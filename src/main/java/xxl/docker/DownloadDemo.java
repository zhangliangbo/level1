package xxl.docker;

import org.apache.commons.cli.*;
import xxl.mathematica.Select;

import java.util.List;
import java.util.Scanner;

public class DownloadDemo {
  public static void main(String[] args) {
    String optImage = "image";
    String optOfficial = "official";
    String optLatest = "latest";
    Options options = new Options()
        .addOption(Option.builder(optImage).hasArg().desc("镜像名称").build())
        .addOption(Option.builder(optOfficial).desc("是否必须官方镜像").build())
        .addOption(Option.builder(optLatest).desc("是否必须最新镜像").build());
    if (args.length == 0) {
      System.err.println(options);
      return;
    }
    CommandLine cli;
    try {
      cli = new DefaultParser().parse(options, args);
    } catch (ParseException e) {
      System.err.println(options);
      return;
    }
    if (!cli.hasOption(optImage)) {
      System.err.println("请指定镜像名称");
      return;
    }
    String image = cli.getOptionValue(optImage, "portainer");
    boolean official = cli.hasOption(optOfficial);
    boolean latest = cli.hasOption(optLatest);
    Docker docker = new Docker();
    ImageInfo imageInfo = null;
    while (true) {
      System.out.println("查找镜像信息...");
      List<ImageInfo> imageInfoList = docker.search(image);
      if (imageInfoList != null) {
        if (imageInfoList.size() > 0) {
          List<ImageInfo> officialImage = Select.select(imageInfoList, ImageInfo::isOfficial);
          if (officialImage.size() > 0) {
            imageInfo = officialImage.get(0);
          } else {
            if (!official) {
              imageInfo = imageInfoList.get(0);
            }
          }
        }
        break;
      }
    }
    if (imageInfo == null) {
      System.err.println("未找到镜像");
      return;
    }
    System.out.println(imageInfo);
    String tag = "latest";
    if (!latest) {
      System.out.println("请输入tag:");
      Scanner sc = new Scanner(System.in);
      if (sc.hasNextLine()) {
        tag = sc.nextLine();
      }
    }
    String name = imageInfo.getName() + ":" + tag;
    System.out.println("开始下载镜像" + name);
    while (true) {
      if (docker.pull(name)) {
        System.out.println("下载完成");
        break;
      } else {
        System.err.println("下载失败，开始重试");
      }
      try {
        Thread.sleep(500);
      } catch (InterruptedException e) {
        System.err.println(e.getMessage());
      }
    }
  }
}
