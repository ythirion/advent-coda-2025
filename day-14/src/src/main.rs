use std::collections::HashSet;

mod tests;

fn main() {
    println!("Hello, world!");
}

type Coordinate = (i32, i32);

const ALLOWED_CHARS: [char; 4] = ['N', 'E', 'W', 'S'];

const NORTH: char = 'N';
const SOUTH: char = 'S';
const EAST: char = 'E';
const WEST: char = 'W';

pub fn count_houses(instructions: &str) -> Result<i32, &str> {
    parse_instructions(instructions)
        .map(|instructions| count_houses_safely(instructions))
}

fn parse_instructions(instructions: &str) -> Result<&str, &str> {
    if instructions.is_empty() {
        return Err("Merci de fournir des instructions");
    } else if contains_invalid_character(instructions) {
        return Err("Les instructions sont invalides")
    }
    Ok(instructions)
}

fn contains_invalid_character(instructions: &str) -> bool {
    instructions
        .chars()
        .any(|c| !is_valid(c))
}

fn is_valid(c: char) -> bool {
    ALLOWED_CHARS.contains(&c)
}

fn count_houses_safely(instructions: &str) -> i32 {
    let mut current_position: Coordinate = (0, 0);
    let mut houses: HashSet<Coordinate> = HashSet::new();

    houses.insert(current_position);

    instructions
        .chars()
        .for_each(|c| {
            current_position = move_to(current_position, c);
            houses.insert(current_position);
        });

    houses.len() as i32
}

fn move_to((x, y): Coordinate, direction: char) -> Coordinate {
    match direction {
        NORTH => (x, y + 1),
        SOUTH => (x, y - 1),
        EAST => (x + 1, y),
        WEST => (x - 1, y),
        _ => panic!("Invalid direction"),
    }
}