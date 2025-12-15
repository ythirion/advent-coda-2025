#[cfg(test)]
mod tests {
    use crate::count_houses;
    use parameterized::parameterized;
    use std::fs;

    #[test]
    fn fail_for_empty_string() {
        assert_eq!(
            count_houses(""),
            Err("Merci de fournir des instructions")
        );
    }

    #[parameterized(instructions = {"0","NEWNSX","0123456789", "\n"})]
    fn fail_for_invalid_characters(instructions: &str) {
        assert_eq!(
            count_houses(instructions),
            Err("Les instructions sont invalides")
        );
    }

    #[parameterized(
            instructions = { "NNSS", "NNESESW", "NESWNESW", "NENENENEN", "NEEEESSSSWWWW" },
            expected_result = { 3, 8, 4, 10, 14 }
    )]
    fn success_for_valid_instructions(instructions: &str, expected_result: i32) {
        assert_eq!(
            count_houses(instructions),
            Ok(expected_result)
        );
    }

    #[test]
    fn calculate_result_for_real_steps() {
        assert_eq!(
            count_houses(
                &get_content_as_string("rsc/steps")
            ), Ok(5260)
        );
    }

    fn get_content_as_string(path: &str) -> String {
        fs::read_to_string(path)
            .map_err(|_| "Erreur lors de la lecture du fichier")
            .map(|content| content.trim().to_string())
            .unwrap()
    }
}