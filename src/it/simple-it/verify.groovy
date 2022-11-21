import java.nio.file.Files
import java.nio.file.Paths

def content = Files.readString(Paths.get("target/it/simple-it/pom.xml"))
println content
assert !content.contains("  <artifactId>simple-it</artifactId>\n" +
        "  <version>0.0.0</version>")
