use std::error::Error;
use std::{fs, env};

pub struct MiniGrep<'a> {
    pub parameter: &'a Paramater,
}

impl<'a> MiniGrep<'a> {
    pub fn new(parameter: &'a Paramater) -> MiniGrep<'a> {
        MiniGrep { parameter }
    }

    pub fn search(&self, content: &'a String) -> Vec<&'a str> {
        match self.parameter.caseinsensitive {
            true => self.search_insensitive(content),
            _ => self.search_sensitive(content),
        }
    }

    pub fn search_sensitive(&self, content: &'a String) -> Vec<&'a str> {
        let mut matches = Vec::new();
        for line in content.lines() {
            if line.contains(&self.parameter.query) {
                matches.push(line);
            }
        }
        matches
    }

    pub fn search_insensitive(&self, content: &'a String) -> Vec<&'a str> {
        let mut matches = Vec::new();
        for line in content.lines() {
            if line.to_lowercase().contains(&self.parameter.query.to_lowercase()) {
                matches.push(line);
            }
        }
        matches
    }
}

pub struct Paramater {
    pub query: String,
    pub filename: String,
    pub caseinsensitive: bool,
}

impl Paramater {
    pub fn new(args: &Vec<String>) -> Result<Paramater, &str> {
        if args.len() < 3 {
            return Err("not enough args");
        }

        let caseinsensitive = env::var("CASE_INSENSITIVE").is_ok();

        Ok(Paramater {
            query: args[1].clone(),
            filename: args[2].clone(),
            caseinsensitive,
        })
    }
}

pub struct Reader<'a> {
    pub filepath: &'a String,
}

impl<'a> Reader<'a> {
    pub fn new(filepath: &String) -> Reader {
        Reader { filepath }
    }

    pub fn parse(&self, mut content: String) -> Result<String, Box<dyn Error>> {
        content = fs::read_to_string(self.filepath)?;
        Ok(content)
    }
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn match_query_sensitive() {
        let params = Paramater {
            query: "MyTrait".to_string(),
            filename: "sample.txt".to_string(),
            caseinsensitive: false,
        };

        let reader = Reader::new(&params.filename);
        let mut content = String::new();
        content = reader.parse(content).unwrap();

        let minigrep = MiniGrep::new(&params);
        assert_eq!(2, minigrep.search_sensitive(&content).len())
    }

    #[test]
    fn match_query_insensitive() {
        let params = Paramater {
            query: "mytrait".to_string(),
            filename: "sample.txt".to_string(),
            caseinsensitive: true,
        };

        let reader = Reader::new(&params.filename);
        let mut content = String::new();
        content = reader.parse(content).unwrap();

        let minigrep = MiniGrep::new(&params);
        assert_eq!(2, minigrep.search_insensitive(&content).len())
    }
}
