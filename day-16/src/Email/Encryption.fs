namespace Email

open System
open System.IO
open System.Security.Cryptography
open System.Text

module Encryption =    
    type Configuration = { Key: byte[]; Iv: byte[] }
    let private hashData hash (input: string) = input |> Encoding.UTF8.GetBytes |> hash
    let private encodeStringToBase64 str = str |> Convert.ToBase64String |> Convert.FromBase64String
    let private encodeKey key = key |> hashData SHA256.HashData |> encodeStringToBase64
    let private encodeIv iv = iv |> hashData MD5.HashData |> encodeStringToBase64
    let createConfiguration key iv = { Key = encodeKey key; Iv = encodeIv iv }
    let decrypt configuration encryptedText =
        let decryptor = Aes.Create().CreateDecryptor(configuration.Key, configuration.Iv)
        
        encryptedText
            |> Convert.FromBase64String
            |> fun bytes -> new MemoryStream(bytes)
            |> fun ms -> new CryptoStream(ms, decryptor, CryptoStreamMode.Read)
            |> fun cs -> new StreamReader(cs)
            |> _.ReadToEnd()