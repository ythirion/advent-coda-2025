# [Jour 21 – Démasquer l’elfe malveillant](https://coda-school.github.io/advent-2025/?day=21)
## Résultat de mon investigation `Jupyter Notebook`
Voici l'export de mon `notebook` qui m'a servi à identifier le suspect.

Ce notebook t'accompagne pour enquêter dans la base SQLite **`elf_challenge.db`**.
Ton objectif : **identifier l'elfe malveillant**, démontrer la preuve avec **au moins 2 requêtes** et proposer **1 mesure corrective**.

**Tables** : `person`, `gift`, `transactions`, `access_log`.

---
**Conseil** : travaille par hypothèses, note tes trouvailles, et corrèle les logs d'accès avec les transactions autour d'horaires atypiques.


## 1) Préparation de l'environnement

- On va utiliser `sqlite3` et `pandas`.
- La cellule ci-dessous **vérifie** que la base `elf_challenge.db` existe.

```python
import os, sqlite3, random
from datetime import datetime, timedelta

DB_PATH = "elf_challenge.db"

def ensure_db(db_path: str):
    if os.path.exists(db_path):
        print("✔ Base SQLite trouvée :", db_path)
        return
    
    print("⚠ Base absente...", db_path)
   
ensure_db(DB_PATH)
```

✔ Base SQLite trouvée : elf_challenge.db

## 2) Connexion & aperçu du schéma

```python
import sqlite3
import pandas as pd
conn = sqlite3.connect(DB_PATH)

# Compter les lignes par table
q = (
    "SELECT 'person' as table_name, COUNT(*) as count FROM person "
    "UNION ALL SELECT 'gift', COUNT(*) FROM gift "
    "UNION ALL SELECT 'transactions', COUNT(*) FROM transactions "
    "UNION ALL SELECT 'access_log', COUNT(*) FROM access_log;"
)
pd.read_sql_query(q, conn)
```


```python
# Aperçu des colonnes de chaque table (pragma)
tables = ['person','gift','transactions','access_log']
for t in tables:
    df = pd.read_sql_query(f"PRAGMA table_info({t});", conn)
    print(f"\n-- {t} --")
    display(df)
```

-- person --

<div>
<table border="1" class="dataframe">
  <thead>
    <tr style="text-align: right;">
      <th></th>
      <th>cid</th>
      <th>name</th>
      <th>type</th>
      <th>notnull</th>
      <th>dflt_value</th>
      <th>pk</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <th>0</th>
      <td>0</td>
      <td>person_id</td>
      <td>INTEGER</td>
      <td>0</td>
      <td>None</td>
      <td>1</td>
    </tr>
    <tr>
      <th>1</th>
      <td>1</td>
      <td>full_name</td>
      <td>TEXT</td>
      <td>1</td>
      <td>None</td>
      <td>0</td>
    </tr>
    <tr>
      <th>2</th>
      <td>2</td>
      <td>role</td>
      <td>TEXT</td>
      <td>0</td>
      <td>None</td>
      <td>0</td>
    </tr>
    <tr>
      <th>3</th>
      <td>3</td>
      <td>hire_date</td>
      <td>DATE</td>
      <td>0</td>
      <td>None</td>
      <td>0</td>
    </tr>
    <tr>
      <th>4</th>
      <td>4</td>
      <td>notes</td>
      <td>TEXT</td>
      <td>0</td>
      <td>None</td>
      <td>0</td>
    </tr>
  </tbody>
</table>
</div>

-- gift --

<div>
<table border="1" class="dataframe">
  <thead>
    <tr style="text-align: right;">
      <th></th>
      <th>cid</th>
      <th>name</th>
      <th>type</th>
      <th>notnull</th>
      <th>dflt_value</th>
      <th>pk</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <th>0</th>
      <td>0</td>
      <td>gift_id</td>
      <td>INTEGER</td>
      <td>0</td>
      <td>None</td>
      <td>1</td>
    </tr>
    <tr>
      <th>1</th>
      <td>1</td>
      <td>name</td>
      <td>TEXT</td>
      <td>0</td>
      <td>None</td>
      <td>0</td>
    </tr>
    <tr>
      <th>2</th>
      <td>2</td>
      <td>status</td>
      <td>TEXT</td>
      <td>0</td>
      <td>None</td>
      <td>0</td>
    </tr>
    <tr>
      <th>3</th>
      <td>3</td>
      <td>assigned_to</td>
      <td>INTEGER</td>
      <td>0</td>
      <td>None</td>
      <td>0</td>
    </tr>
    <tr>
      <th>4</th>
      <td>4</td>
      <td>created_at</td>
      <td>DATETIME</td>
      <td>0</td>
      <td>None</td>
      <td>0</td>
    </tr>
  </tbody>
</table>
</div>

-- transactions --

<div>
<table border="1" class="dataframe">
  <thead>
    <tr style="text-align: right;">
      <th></th>
      <th>cid</th>
      <th>name</th>
      <th>type</th>
      <th>notnull</th>
      <th>dflt_value</th>
      <th>pk</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <th>0</th>
      <td>0</td>
      <td>tx_id</td>
      <td>INTEGER</td>
      <td>0</td>
      <td>None</td>
      <td>1</td>
    </tr>
    <tr>
      <th>1</th>
      <td>1</td>
      <td>gift_id</td>
      <td>INTEGER</td>
      <td>1</td>
      <td>None</td>
      <td>0</td>
    </tr>
    <tr>
      <th>2</th>
      <td>2</td>
      <td>actor_id</td>
      <td>INTEGER</td>
      <td>1</td>
      <td>None</td>
      <td>0</td>
    </tr>
    <tr>
      <th>3</th>
      <td>3</td>
      <td>action</td>
      <td>TEXT</td>
      <td>1</td>
      <td>None</td>
      <td>0</td>
    </tr>
    <tr>
      <th>4</th>
      <td>4</td>
      <td>details</td>
      <td>TEXT</td>
      <td>0</td>
      <td>None</td>
      <td>0</td>
    </tr>
    <tr>
      <th>5</th>
      <td>5</td>
      <td>tx_ts</td>
      <td>DATETIME</td>
      <td>1</td>
      <td>None</td>
      <td>0</td>
    </tr>
  </tbody>
</table>
</div>

-- access_log --

<div>
<table border="1" class="dataframe">
  <thead>
    <tr style="text-align: right;">
      <th></th>
      <th>cid</th>
      <th>name</th>
      <th>type</th>
      <th>notnull</th>
      <th>dflt_value</th>
      <th>pk</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <th>0</th>
      <td>0</td>
      <td>log_id</td>
      <td>INTEGER</td>
      <td>0</td>
      <td>None</td>
      <td>1</td>
    </tr>
    <tr>
      <th>1</th>
      <td>1</td>
      <td>person_id</td>
      <td>INTEGER</td>
      <td>0</td>
      <td>None</td>
      <td>0</td>
    </tr>
    <tr>
      <th>2</th>
      <td>2</td>
      <td>ts</td>
      <td>DATETIME</td>
      <td>1</td>
      <td>None</td>
      <td>0</td>
    </tr>
    <tr>
      <th>3</th>
      <td>3</td>
      <td>ip_addr</td>
      <td>TEXT</td>
      <td>0</td>
      <td>None</td>
      <td>0</td>
    </tr>
    <tr>
      <th>4</th>
      <td>4</td>
      <td>device</td>
      <td>TEXT</td>
      <td>0</td>
      <td>None</td>
      <td>0</td>
    </tr>
    <tr>
      <th>5</th>
      <td>5</td>
      <td>location</td>
      <td>TEXT</td>
      <td>0</td>
      <td>None</td>
      <td>0</td>
    </tr>
    <tr>
      <th>6</th>
      <td>6</td>
      <td>action</td>
      <td>TEXT</td>
      <td>0</td>
      <td>None</td>
      <td>0</td>
    </tr>
    <tr>
      <th>7</th>
      <td>7</td>
      <td>success</td>
      <td>INTEGER</td>
      <td>0</td>
      <td>None</td>
      <td>0</td>
    </tr>
  </tbody>
</table>
</div>

## 3) Échauffement — requêtes simples

1. **Lister** les personnes avec leur rôle.
2. **Compter** combien de cadeaux par statut (`status`).
3. **Top** 10 transactions les plus récentes (avec nom de l'acteur).

```python
# 1) Personnes / rôles
pd.read_sql_query("""
SELECT person_id, full_name, role
FROM person
ORDER BY role, full_name;
""", conn)
```

<div>
<table border="1" class="dataframe">
  <thead>
    <tr style="text-align: right;">
      <th></th>
      <th>person_id</th>
      <th>full_name</th>
      <th>role</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <th>0</th>
      <td>14</td>
      <td>E. L. Finkel</td>
      <td>elf</td>
    </tr>
    <tr>
      <th>1</th>
      <td>15</td>
      <td>Merry G. Night</td>
      <td>elf</td>
    </tr>
    <tr>
      <th>2</th>
      <td>12</td>
      <td>Heloise Bernard</td>
      <td>staff</td>
    </tr>
    <tr>
      <th>3</th>
      <td>11</td>
      <td>Marc Leroy</td>
      <td>staff</td>
    </tr>
    <tr>
      <th>4</th>
      <td>16</td>
      <td>Service Admin</td>
      <td>staff</td>
    </tr>
    <tr>
      <th>5</th>
      <td>13</td>
      <td>Thomas Girard</td>
      <td>staff</td>
    </tr>
    <tr>
      <th>6</th>
      <td>4</td>
      <td>Amélie Roy</td>
      <td>student</td>
    </tr>
    <tr>
      <th>7</th>
      <td>9</td>
      <td>Camille Lenoir</td>
      <td>student</td>
    </tr>
    <tr>
      <th>8</th>
      <td>1</td>
      <td>Clara Dupont</td>
      <td>student</td>
    </tr>
    <tr>
      <th>9</th>
      <td>6</td>
      <td>Emma Durand</td>
      <td>student</td>
    </tr>
    <tr>
      <th>10</th>
      <td>8</td>
      <td>Julien Faure</td>
      <td>student</td>
    </tr>
    <tr>
      <th>11</th>
      <td>3</td>
      <td>Lucas Martin</td>
      <td>student</td>
    </tr>
    <tr>
      <th>12</th>
      <td>7</td>
      <td>Léa Moreau</td>
      <td>student</td>
    </tr>
    <tr>
      <th>13</th>
      <td>5</td>
      <td>Noah Petit</td>
      <td>student</td>
    </tr>
    <tr>
      <th>14</th>
      <td>2</td>
      <td>Sophie Martin</td>
      <td>student</td>
    </tr>
    <tr>
      <th>15</th>
      <td>10</td>
      <td>Theo Blanc</td>
      <td>student</td>
    </tr>
  </tbody>
</table>
</div>

```python
# 2) Cadeaux par statut
pd.read_sql_query("""
SELECT status, COUNT(*) as nb
FROM gift
GROUP BY status
ORDER BY nb DESC;
""", conn)
```

<div>
<table border="1" class="dataframe">
  <thead>
    <tr style="text-align: right;">
      <th></th>
      <th>status</th>
      <th>nb</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <th>0</th>
      <td>in_stock</td>
      <td>33</td>
    </tr>
    <tr>
      <th>1</th>
      <td>assigned</td>
      <td>9</td>
    </tr>
    <tr>
      <th>2</th>
      <td>missing</td>
      <td>5</td>
    </tr>
    <tr>
      <th>3</th>
      <td>sent</td>
      <td>3</td>
    </tr>
  </tbody>
</table>
</div>

```python
# 3) Top 10 transactions récentes
pd.read_sql_query("""
SELECT t.tx_id, t.gift_id, p.full_name AS actor, t.action, t.details, t.tx_ts
FROM transactions t
JOIN person p ON p.person_id = t.actor_id
ORDER BY t.tx_ts DESC
LIMIT 10;
""", conn)
```

<div>
<table border="1" class="dataframe">
  <thead>
    <tr style="text-align: right;">
      <th></th>
      <th>tx_id</th>
      <th>gift_id</th>
      <th>actor</th>
      <th>action</th>
      <th>details</th>
      <th>tx_ts</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <th>0</th>
      <td>23</td>
      <td>42</td>
      <td>Theo Blanc</td>
      <td>note</td>
      <td>no defects</td>
      <td>2025-11-30 20:59:00</td>
    </tr>
    <tr>
      <th>1</th>
      <td>177</td>
      <td>40</td>
      <td>Julien Faure</td>
      <td>assign</td>
      <td>assign to Merry G. Night</td>
      <td>2025-11-30 19:19:00</td>
    </tr>
    <tr>
      <th>2</th>
      <td>171</td>
      <td>24</td>
      <td>Camille Lenoir</td>
      <td>change_status</td>
      <td>sent to student</td>
      <td>2025-11-30 15:51:00</td>
    </tr>
    <tr>
      <th>3</th>
      <td>148</td>
      <td>29</td>
      <td>Amélie Roy</td>
      <td>check_inventory</td>
      <td>inventory ok</td>
      <td>2025-11-30 15:00:00</td>
    </tr>
    <tr>
      <th>4</th>
      <td>169</td>
      <td>3</td>
      <td>Heloise Bernard</td>
      <td>change_status</td>
      <td>marked missing</td>
      <td>2025-11-30 12:20:00</td>
    </tr>
    <tr>
      <th>5</th>
      <td>61</td>
      <td>45</td>
      <td>Merry G. Night</td>
      <td>assign</td>
      <td>assign to Julien Faure</td>
      <td>2025-11-30 09:29:00</td>
    </tr>
    <tr>
      <th>6</th>
      <td>58</td>
      <td>50</td>
      <td>Lucas Martin</td>
      <td>note</td>
      <td>no defects</td>
      <td>2025-11-29 18:36:00</td>
    </tr>
    <tr>
      <th>7</th>
      <td>179</td>
      <td>32</td>
      <td>Amélie Roy</td>
      <td>assign</td>
      <td>assign to Julien Faure</td>
      <td>2025-11-29 16:08:00</td>
    </tr>
    <tr>
      <th>8</th>
      <td>44</td>
      <td>44</td>
      <td>Marc Leroy</td>
      <td>assign</td>
      <td>assign to Amélie Roy</td>
      <td>2025-11-29 12:11:00</td>
    </tr>
    <tr>
      <th>9</th>
      <td>200</td>
      <td>33</td>
      <td>E. L. Finkel</td>
      <td>assign</td>
      <td>assign to Amélie Roy</td>
      <td>2025-11-28 18:41:00</td>
    </tr>
  </tbody>
</table>
</div>



## 4) Investigation — pistes guidées
### 4.1 Actions nocturnes (00:00–05:00)
- Qui agit la nuit ?
- Y a‑t‑il des **elfes** dans la liste ?

```python
pd.read_sql_query("""
SELECT p.person_id, p.full_name, a.ts, a.action
FROM access_log a
JOIN person p ON p.person_id = a.person_id
WHERE time(a.ts) BETWEEN '00:00:00' AND '05:00:00'
ORDER BY a.ts;
""", conn)
```

<div>
<table border="1" class="dataframe">
  <thead>
    <tr style="text-align: right;">
      <th></th>
      <th>person_id</th>
      <th>full_name</th>
      <th>ts</th>
      <th>action</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <th>0</th>
      <td>15</td>
      <td>Merry G. Night</td>
      <td>2025-11-18 02:13:00</td>
      <td>edit_gift</td>
    </tr>
    <tr>
      <th>1</th>
      <td>11</td>
      <td>Marc Leroy</td>
      <td>2025-11-18 02:14:00</td>
      <td>view_gift</td>
    </tr>
    <tr>
      <th>2</th>
      <td>15</td>
      <td>Merry G. Night</td>
      <td>2025-11-20 01:45:00</td>
      <td>edit_gift</td>
    </tr>
    <tr>
      <th>3</th>
      <td>11</td>
      <td>Marc Leroy</td>
      <td>2025-11-20 01:46:00</td>
      <td>view_gift</td>
    </tr>
    <tr>
      <th>4</th>
      <td>15</td>
      <td>Merry G. Night</td>
      <td>2025-11-22 03:05:00</td>
      <td>edit_gift</td>
    </tr>
    <tr>
      <th>5</th>
      <td>11</td>
      <td>Marc Leroy</td>
      <td>2025-11-22 03:06:00</td>
      <td>view_gift</td>
    </tr>
  </tbody>
</table>
</div>



### 4.2 Transactions suspectes (emoji 🧝, 'missing')
- Chercher des **indices textuels**.

```python
pd.read_sql_query("""
SELECT t.tx_id, t.gift_id, t.actor_id, p.full_name, t.action, t.details, t.tx_ts
FROM transactions t
JOIN person p ON p.person_id = t.actor_id
WHERE t.details LIKE '%🧝%' OR t.details LIKE '%missing%'
ORDER BY t.tx_ts DESC;
""", conn)
```

<div>
<table border="1" class="dataframe">
  <thead>
    <tr style="text-align: right;">
      <th></th>
      <th>tx_id</th>
      <th>gift_id</th>
      <th>actor_id</th>
      <th>full_name</th>
      <th>action</th>
      <th>details</th>
      <th>tx_ts</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <th>0</th>
      <td>169</td>
      <td>3</td>
      <td>12</td>
      <td>Heloise Bernard</td>
      <td>change_status</td>
      <td>marked missing</td>
      <td>2025-11-30 12:20:00</td>
    </tr>
    <tr>
      <th>1</th>
      <td>205</td>
      <td>47</td>
      <td>15</td>
      <td>Merry G. Night</td>
      <td>change_status</td>
      <td>marked missing 🧝</td>
      <td>2025-11-22 03:05:00</td>
    </tr>
    <tr>
      <th>2</th>
      <td>206</td>
      <td>47</td>
      <td>15</td>
      <td>Merry G. Night</td>
      <td>transfer</td>
      <td>moved to secret shelf 🧝</td>
      <td>2025-11-22 02:58:00</td>
    </tr>
    <tr>
      <th>3</th>
      <td>203</td>
      <td>36</td>
      <td>15</td>
      <td>Merry G. Night</td>
      <td>change_status</td>
      <td>marked missing 🧝</td>
      <td>2025-11-20 01:45:00</td>
    </tr>
    <tr>
      <th>4</th>
      <td>204</td>
      <td>36</td>
      <td>15</td>
      <td>Merry G. Night</td>
      <td>transfer</td>
      <td>moved to secret shelf 🧝</td>
      <td>2025-11-20 01:38:00</td>
    </tr>
    <tr>
      <th>5</th>
      <td>201</td>
      <td>50</td>
      <td>15</td>
      <td>Merry G. Night</td>
      <td>change_status</td>
      <td>marked missing 🧝</td>
      <td>2025-11-18 02:13:00</td>
    </tr>
    <tr>
      <th>6</th>
      <td>207</td>
      <td>14</td>
      <td>15</td>
      <td>Merry G. Night</td>
      <td>change_status</td>
      <td>marked missing 🧝</td>
      <td>2025-11-18 02:13:00</td>
    </tr>
    <tr>
      <th>7</th>
      <td>202</td>
      <td>50</td>
      <td>15</td>
      <td>Merry G. Night</td>
      <td>transfer</td>
      <td>moved to secret shelf 🧝</td>
      <td>2025-11-18 02:06:00</td>
    </tr>
    <tr>
      <th>8</th>
      <td>208</td>
      <td>14</td>
      <td>15</td>
      <td>Merry G. Night</td>
      <td>transfer</td>
      <td>moved to secret shelf 🧝</td>
      <td>2025-11-18 02:06:00</td>
    </tr>
    <tr>
      <th>9</th>
      <td>136</td>
      <td>27</td>
      <td>3</td>
      <td>Lucas Martin</td>
      <td>change_status</td>
      <td>marked missing</td>
      <td>2025-11-15 09:41:00</td>
    </tr>
    <tr>
      <th>10</th>
      <td>153</td>
      <td>16</td>
      <td>10</td>
      <td>Theo Blanc</td>
      <td>change_status</td>
      <td>marked missing</td>
      <td>2025-11-14 09:48:00</td>
    </tr>
    <tr>
      <th>11</th>
      <td>191</td>
      <td>8</td>
      <td>1</td>
      <td>Clara Dupont</td>
      <td>change_status</td>
      <td>marked missing</td>
      <td>2025-11-12 20:50:00</td>
    </tr>
    <tr>
      <th>12</th>
      <td>91</td>
      <td>18</td>
      <td>11</td>
      <td>Marc Leroy</td>
      <td>change_status</td>
      <td>marked missing</td>
      <td>2025-11-09 15:15:00</td>
    </tr>
    <tr>
      <th>13</th>
      <td>186</td>
      <td>30</td>
      <td>3</td>
      <td>Lucas Martin</td>
      <td>change_status</td>
      <td>marked missing</td>
      <td>2025-11-02 20:21:00</td>
    </tr>
    <tr>
      <th>14</th>
      <td>119</td>
      <td>10</td>
      <td>9</td>
      <td>Camille Lenoir</td>
      <td>change_status</td>
      <td>marked missing</td>
      <td>2025-11-02 10:50:00</td>
    </tr>
  </tbody>
</table>
</div>



### 4.3 IPs & devices partagés
- Plusieurs comptes depuis la **même IP / device** ?

```python
pd.read_sql_query("""
SELECT ip_addr, device, COUNT(DISTINCT person_id) as nb_accounts,
GROUP_CONCAT(DISTINCT person_id) as person_ids
FROM access_log
GROUP BY ip_addr, device
HAVING nb_accounts > 1
ORDER BY nb_accounts DESC;
""", conn)
```

<div>
<table border="1" class="dataframe">
  <thead>
    <tr style="text-align: right;">
      <th></th>
      <th>ip_addr</th>
      <th>device</th>
      <th>nb_accounts</th>
      <th>person_ids</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <th>0</th>
      <td>10.0.0.45</td>
      <td>phone-A</td>
      <td>10</td>
      <td>8,12,10,13,6,9,1,14,11,2</td>
    </tr>
    <tr>
      <th>1</th>
      <td>10.0.0.45</td>
      <td>raspberry-pi-3</td>
      <td>10</td>
      <td>12,2,14,3,7,13,5,1,15,11</td>
    </tr>
    <tr>
      <th>2</th>
      <td>10.0.4.12</td>
      <td>raspberry-pi-3</td>
      <td>10</td>
      <td>11,4,1,9,16,7,8,13,10,14</td>
    </tr>
    <tr>
      <th>3</th>
      <td>10.0.5.9</td>
      <td>tablet-7</td>
      <td>10</td>
      <td>11,12,16,14,6,7,3,9,10,4</td>
    </tr>
    <tr>
      <th>4</th>
      <td>172.16.8.10</td>
      <td>phone-A</td>
      <td>10</td>
      <td>5,6,12,9,13,8,14,2,3,16</td>
    </tr>
    <tr>
      <th>5</th>
      <td>10.0.0.45</td>
      <td>laptop-12</td>
      <td>9</td>
      <td>5,6,12,11,9,16,15,14,8</td>
    </tr>
    <tr>
      <th>6</th>
      <td>10.0.4.12</td>
      <td>laptop-12</td>
      <td>9</td>
      <td>1,8,7,4,16,15,6,13,9</td>
    </tr>
    <tr>
      <th>7</th>
      <td>10.0.5.9</td>
      <td>laptop-12</td>
      <td>9</td>
      <td>3,16,13,1,7,14,5,6,4</td>
    </tr>
    <tr>
      <th>8</th>
      <td>10.0.0.45</td>
      <td>tablet-7</td>
      <td>8</td>
      <td>9,15,7,16,6,12,3,14</td>
    </tr>
    <tr>
      <th>9</th>
      <td>10.0.5.9</td>
      <td>desktop-3</td>
      <td>8</td>
      <td>2,12,15,16,10,8,11,13</td>
    </tr>
    <tr>
      <th>10</th>
      <td>172.16.8.10</td>
      <td>desktop-3</td>
      <td>8</td>
      <td>13,10,1,7,3,8,15,9</td>
    </tr>
    <tr>
      <th>11</th>
      <td>172.16.8.10</td>
      <td>tablet-7</td>
      <td>8</td>
      <td>14,15,6,16,10,3,8,11</td>
    </tr>
    <tr>
      <th>12</th>
      <td>10.0.0.45</td>
      <td>desktop-3</td>
      <td>7</td>
      <td>10,3,12,11,13,5,1</td>
    </tr>
    <tr>
      <th>13</th>
      <td>10.0.0.46</td>
      <td>desktop-3</td>
      <td>7</td>
      <td>11,1,6,15,4,10,8</td>
    </tr>
    <tr>
      <th>14</th>
      <td>10.0.0.46</td>
      <td>tablet-7</td>
      <td>7</td>
      <td>1,12,15,13,2,10,5</td>
    </tr>
    <tr>
      <th>15</th>
      <td>10.0.4.12</td>
      <td>desktop-3</td>
      <td>7</td>
      <td>6,14,5,12,3,13,7</td>
    </tr>
    <tr>
      <th>16</th>
      <td>10.0.4.12</td>
      <td>phone-A</td>
      <td>7</td>
      <td>16,3,11,9,4,1,2</td>
    </tr>
    <tr>
      <th>17</th>
      <td>10.0.4.12</td>
      <td>tablet-7</td>
      <td>7</td>
      <td>14,11,16,4,12,8,6</td>
    </tr>
    <tr>
      <th>18</th>
      <td>10.0.5.9</td>
      <td>phone-A</td>
      <td>7</td>
      <td>8,4,6,10,13,3,5</td>
    </tr>
    <tr>
      <th>19</th>
      <td>10.0.5.9</td>
      <td>raspberry-pi-3</td>
      <td>7</td>
      <td>12,3,14,13,11,5,4</td>
    </tr>
    <tr>
      <th>20</th>
      <td>172.16.8.10</td>
      <td>laptop-12</td>
      <td>7</td>
      <td>15,5,2,7,8,16,9</td>
    </tr>
    <tr>
      <th>21</th>
      <td>10.0.0.46</td>
      <td>laptop-12</td>
      <td>6</td>
      <td>4,9,13,5,12,6</td>
    </tr>
    <tr>
      <th>22</th>
      <td>172.16.8.10</td>
      <td>raspberry-pi-3</td>
      <td>6</td>
      <td>5,6,3,1,12,8</td>
    </tr>
    <tr>
      <th>23</th>
      <td>10.0.0.46</td>
      <td>raspberry-pi-3</td>
      <td>5</td>
      <td>11,4,6,15,13</td>
    </tr>
    <tr>
      <th>24</th>
      <td>10.0.0.46</td>
      <td>phone-A</td>
      <td>3</td>
      <td>12,14,11</td>
    </tr>
  </tbody>
</table>
</div>



### 4.4 Corrélation `transactions` ↔ `access_log` (± 2 minutes)
- Valider qu'une action a eu lieu **pendant une session** donnée.

```python
pd.read_sql_query("""
SELECT t.tx_id, t.gift_id, t.actor_id, p.full_name as actor, t.details, t.tx_ts,
       a.log_id, a.person_id, a.ip_addr, a.device, a.location, a.action as access_action, a.ts as access_ts
FROM transactions t
JOIN person p ON p.person_id = t.actor_id
LEFT JOIN access_log a
  ON a.person_id = t.actor_id
 AND ABS(strftime('%s', t.tx_ts) - strftime('%s', a.ts)) <= 120 // < 2 minutes
WHERE t.details LIKE '%missing%'
ORDER BY t.tx_ts DESC
LIMIT 50;
""", conn)
```

<div>
<table border="1" class="dataframe">
  <thead>
    <tr style="text-align: right;">
      <th></th>
      <th>tx_id</th>
      <th>gift_id</th>
      <th>actor_id</th>
      <th>actor</th>
      <th>details</th>
      <th>tx_ts</th>
      <th>log_id</th>
      <th>person_id</th>
      <th>ip_addr</th>
      <th>device</th>
      <th>location</th>
      <th>access_action</th>
      <th>access_ts</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <th>0</th>
      <td>169</td>
      <td>3</td>
      <td>12</td>
      <td>Heloise Bernard</td>
      <td>marked missing</td>
      <td>2025-11-30 12:20:00</td>
      <td>NaN</td>
      <td>NaN</td>
      <td>None</td>
      <td>None</td>
      <td>None</td>
      <td>None</td>
      <td>None</td>
    </tr>
    <tr>
      <th>1</th>
      <td>205</td>
      <td>47</td>
      <td>15</td>
      <td>Merry G. Night</td>
      <td>marked missing 🧝</td>
      <td>2025-11-22 03:05:00</td>
      <td>255.0</td>
      <td>15.0</td>
      <td>10.0.0.45</td>
      <td>raspberry-pi-3</td>
      <td>warehouse</td>
      <td>edit_gift</td>
      <td>2025-11-22 03:05:00</td>
    </tr>
    <tr>
      <th>2</th>
      <td>203</td>
      <td>36</td>
      <td>15</td>
      <td>Merry G. Night</td>
      <td>marked missing 🧝</td>
      <td>2025-11-20 01:45:00</td>
      <td>253.0</td>
      <td>15.0</td>
      <td>10.0.0.45</td>
      <td>raspberry-pi-3</td>
      <td>warehouse</td>
      <td>edit_gift</td>
      <td>2025-11-20 01:45:00</td>
    </tr>
    <tr>
      <th>3</th>
      <td>201</td>
      <td>50</td>
      <td>15</td>
      <td>Merry G. Night</td>
      <td>marked missing 🧝</td>
      <td>2025-11-18 02:13:00</td>
      <td>251.0</td>
      <td>15.0</td>
      <td>10.0.0.45</td>
      <td>raspberry-pi-3</td>
      <td>warehouse</td>
      <td>edit_gift</td>
      <td>2025-11-18 02:13:00</td>
    </tr>
    <tr>
      <th>4</th>
      <td>207</td>
      <td>14</td>
      <td>15</td>
      <td>Merry G. Night</td>
      <td>marked missing 🧝</td>
      <td>2025-11-18 02:13:00</td>
      <td>251.0</td>
      <td>15.0</td>
      <td>10.0.0.45</td>
      <td>raspberry-pi-3</td>
      <td>warehouse</td>
      <td>edit_gift</td>
      <td>2025-11-18 02:13:00</td>
    </tr>
    <tr>
      <th>5</th>
      <td>136</td>
      <td>27</td>
      <td>3</td>
      <td>Lucas Martin</td>
      <td>marked missing</td>
      <td>2025-11-15 09:41:00</td>
      <td>NaN</td>
      <td>NaN</td>
      <td>None</td>
      <td>None</td>
      <td>None</td>
      <td>None</td>
      <td>None</td>
    </tr>
    <tr>
      <th>6</th>
      <td>153</td>
      <td>16</td>
      <td>10</td>
      <td>Theo Blanc</td>
      <td>marked missing</td>
      <td>2025-11-14 09:48:00</td>
      <td>NaN</td>
      <td>NaN</td>
      <td>None</td>
      <td>None</td>
      <td>None</td>
      <td>None</td>
      <td>None</td>
    </tr>
    <tr>
      <th>7</th>
      <td>191</td>
      <td>8</td>
      <td>1</td>
      <td>Clara Dupont</td>
      <td>marked missing</td>
      <td>2025-11-12 20:50:00</td>
      <td>NaN</td>
      <td>NaN</td>
      <td>None</td>
      <td>None</td>
      <td>None</td>
      <td>None</td>
      <td>None</td>
    </tr>
    <tr>
      <th>8</th>
      <td>91</td>
      <td>18</td>
      <td>11</td>
      <td>Marc Leroy</td>
      <td>marked missing</td>
      <td>2025-11-09 15:15:00</td>
      <td>NaN</td>
      <td>NaN</td>
      <td>None</td>
      <td>None</td>
      <td>None</td>
      <td>None</td>
      <td>None</td>
    </tr>
    <tr>
      <th>9</th>
      <td>186</td>
      <td>30</td>
      <td>3</td>
      <td>Lucas Martin</td>
      <td>marked missing</td>
      <td>2025-11-02 20:21:00</td>
      <td>NaN</td>
      <td>NaN</td>
      <td>None</td>
      <td>None</td>
      <td>None</td>
      <td>None</td>
      <td>None</td>
    </tr>
    <tr>
      <th>10</th>
      <td>119</td>
      <td>10</td>
      <td>9</td>
      <td>Camille Lenoir</td>
      <td>marked missing</td>
      <td>2025-11-02 10:50:00</td>
      <td>NaN</td>
      <td>NaN</td>
      <td>None</td>
      <td>None</td>
      <td>None</td>
      <td>None</td>
      <td>None</td>
    </tr>
  </tbody>
</table>
</div>



Au vu de ce qu'on comprend avec les requêtes ci-dessus, on peut s'interroger sur les activités de Merry G. Night...

## 5) Ton enquête — espace de travail

Utilise les cellules ci‑dessous pour :
- Formuler tes hypothèses
- Tester tes requêtes
- Consolider la **preuve** (au moins 2 requêtes pertinentes)


```python
# Requête (activité nocturne, 00:00–05:00)

pd.read_sql_query("""
    SELECT a.ts, a.ip_addr, a.device, a.location, a.action
    FROM access_log a
    WHERE a.person_id = 15
    AND time(a.ts) BETWEEN '00:00:00' AND '05:00:00'
    ORDER BY a.ts;
""", conn)
```

<div>
<table border="1" class="dataframe">
  <thead>
    <tr style="text-align: right;">
      <th></th>
      <th>ts</th>
      <th>ip_addr</th>
      <th>device</th>
      <th>location</th>
      <th>action</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <th>0</th>
      <td>2025-11-18 02:13:00</td>
      <td>10.0.0.45</td>
      <td>raspberry-pi-3</td>
      <td>warehouse</td>
      <td>edit_gift</td>
    </tr>
    <tr>
      <th>1</th>
      <td>2025-11-20 01:45:00</td>
      <td>10.0.0.45</td>
      <td>raspberry-pi-3</td>
      <td>warehouse</td>
      <td>edit_gift</td>
    </tr>
    <tr>
      <th>2</th>
      <td>2025-11-22 03:05:00</td>
      <td>10.0.0.45</td>
      <td>raspberry-pi-3</td>
      <td>warehouse</td>
      <td>edit_gift</td>
    </tr>
  </tbody>
</table>
</div>



On regarde les activités de nuit pour Merry G. Night qui `edit_gift` autour de 2025-11-18 02:13, 2025-11-20 01:45, 2025-11-22 03:05, depuis **10.0.0.45** / **raspberry-pi-3** en warehouse — **activité anormale**.


```python
# Requête (corrélation transactions ↔ accès ±120s, contenu suspect)
pd.read_sql_query("""
    SELECT t.tx_id, t.gift_id, t.action, t.details, t.tx_ts, a.ts AS access_ts, a.ip_addr, a.device, a.location
    FROM transactions t
    JOIN access_log a
    ON a.person_id = t.actor_id
    AND ABS(strftime('%s', t.tx_ts) - strftime('%s', a.ts)) <= 120
    WHERE t.actor_id = 15
    AND (t.details LIKE '%🧝%' OR t.details LIKE '%missing%')
    ORDER BY t.tx_ts;
""", conn)
```

<div>
<table border="1" class="dataframe">
  <thead>
    <tr style="text-align: right;">
      <th></th>
      <th>tx_id</th>
      <th>gift_id</th>
      <th>action</th>
      <th>details</th>
      <th>tx_ts</th>
      <th>access_ts</th>
      <th>ip_addr</th>
      <th>device</th>
      <th>location</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <th>0</th>
      <td>201</td>
      <td>50</td>
      <td>change_status</td>
      <td>marked missing 🧝</td>
      <td>2025-11-18 02:13:00</td>
      <td>2025-11-18 02:13:00</td>
      <td>10.0.0.45</td>
      <td>raspberry-pi-3</td>
      <td>warehouse</td>
    </tr>
    <tr>
      <th>1</th>
      <td>207</td>
      <td>14</td>
      <td>change_status</td>
      <td>marked missing 🧝</td>
      <td>2025-11-18 02:13:00</td>
      <td>2025-11-18 02:13:00</td>
      <td>10.0.0.45</td>
      <td>raspberry-pi-3</td>
      <td>warehouse</td>
    </tr>
    <tr>
      <th>2</th>
      <td>203</td>
      <td>36</td>
      <td>change_status</td>
      <td>marked missing 🧝</td>
      <td>2025-11-20 01:45:00</td>
      <td>2025-11-20 01:45:00</td>
      <td>10.0.0.45</td>
      <td>raspberry-pi-3</td>
      <td>warehouse</td>
    </tr>
    <tr>
      <th>3</th>
      <td>205</td>
      <td>47</td>
      <td>change_status</td>
      <td>marked missing 🧝</td>
      <td>2025-11-22 03:05:00</td>
      <td>2025-11-22 03:05:00</td>
      <td>10.0.0.45</td>
      <td>raspberry-pi-3</td>
      <td>warehouse</td>
    </tr>
  </tbody>
</table>
</div>

Les transactions de **Merry G. Night**
- `change_status` → *marked missing 🧝*, `transfer` → *moved to secret shelf 🧝* sont **synchronisées** avec ses connexions nocturnes
- même IP/device/lieu → lien direct entre sa session et les cadeaux passés manquants

## 6) Conclusion
**Elfe malveillant identifié** : `person_id = 15`, `full_name = Merry G. Night`

Mesure corrective proposée :
> Imposer MFA immédiatement pour tous les comptes sensibles, isoler le device `raspberry-pi-3` (warehouse) pour audit forensique et bloquer l’IP `10.0.0.45`, avec une alerte automatique sur toute action entre 00:00 et 05:00