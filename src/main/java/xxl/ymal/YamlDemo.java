package xxl.ymal;

import io.vavr.collection.Tree;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Map;

public class YamlDemo {
  public static void main(String[] args) throws FileNotFoundException {
    String file = "C:\\Users\\zhang\\Desktop\\application.yml";
    Yaml yaml = new Yaml();
    Map<String, Object> map = yaml.loadAs(new FileInputStream(file), Map.class);
    System.err.println(map);
    Tree.Node<String> root = Tree.of("root");
    for (Map.Entry<String, Object> entry : map.entrySet()) {

    }
  }
}
