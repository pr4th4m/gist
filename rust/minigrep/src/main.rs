use std::env;
use std::process;

use minigrep::MiniGrep;
use minigrep::{Paramater, Reader};

fn main() {
    let args: Vec<String> = env::args().collect();

    let params = Paramater::new(&args).unwrap_or_else(|err| {
        println!("Problem parsing args {}", err);
        process::exit(1);
    });

    let reader = Reader::new(&params.filename);
    let mut content = String::new();
    content = reader.parse(content).unwrap_or_else(|err| {
        println!("Failed to read input file {}", err);
        process::exit(1);
    });

    let minigrep = MiniGrep::new(&params);
    let matches = minigrep.search(&content);
    println!("{:?}", matches);
}
