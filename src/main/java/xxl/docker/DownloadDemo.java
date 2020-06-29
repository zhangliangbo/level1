package xxl.docker;

import org.apache.commons.cli.*;
import xxl.mathematica.audio.Beep;
import xxl.mathematica.list.First;
import xxl.mathematica.list.Select;

import java.util.List;

public class DownloadDemo {
    public static void main(String[] args) {
        String optImage = "image";
        String optTag = "tag";
        String optOfficial = "official";
        Options options = new Options()
                .addOption(Option.builder(optImage).hasArg().desc("镜像名称").build())
                .addOption(Option.builder(optTag).hasArg().desc("镜像tag").build())
                .addOption(Option.builder(optOfficial).desc("是否必须官方镜像").build());
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
        String tag = cli.getOptionValue(optTag, "latest");
        boolean official = cli.hasOption(optOfficial);
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
                            imageInfoList.sort((o1, o2) -> o2.getStars() - o1.getStars());//选择star最多
                            imageInfo = First.first(imageInfoList, null);
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
        String name = imageInfo.getName() + ":" + tag;
        System.out.println("开始下载镜像" + name);
        while (true) {
            if (docker.pull(name)) {
                System.out.println("下载完成");
                break;
            } else {
                System.err.println("下载失败，开始重试");
                Beep.beep();
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                System.err.println(e.getMessage());
            }
        }
    }
}
