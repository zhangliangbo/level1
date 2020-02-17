package xxl.kafka;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.util.List;

public class ConsumerDemo {
  public static void main(String[] args) {
    Options options = new Options().addOption("group", true, "指定消费组");
    CommandLine cli;
    try {
      cli = new DefaultParser().parse(options, args);
    } catch (ParseException e) {
      System.out.println(options);
      return;
    }
    if (cli.hasOption("group")) {
      KfkConsumer consumer = new KfkConsumer(new String[]{"localhost:9094", "localhost:9095", "localhost:9096"}, cli.getOptionValue("group"));
      consumer.subscribe("xxl");
      while (true) {
        List<Record> records = consumer.poll(1000);
        if (records != null) {
          for (Record record : records) {
            System.err.println(record);
            consumer.commitSync();
          }
        }
      }
    } else {
      System.out.println(options);
    }
  }
}
