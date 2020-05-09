package xxl.docker;

import xxl.mathematica.Rule;
import xxl.mathematica.external.External;

import java.util.ArrayList;
import java.util.List;

/**
 * docker下载
 */
public class Docker {
    /**
     * 搜索镜像
     *
     * @param image
     * @return
     */
    public List<ImageInfo> search(String image) {
        Rule<Integer, byte[]> rule;
        try {
            rule = External.runProcess("docker search " + image);
        } catch (Exception e) {
            return null;
        }
        if (rule.getKey() == 0) {
            String[] records = new String(rule.getValue()).split("\n");
            String[] columns = records[0].split(" +");
            int[] starts = new int[columns.length];
            for (int i = 0; i < starts.length; i++) {
                starts[i] = records[0].indexOf(columns[i]);
            }
            List<ImageInfo> res = new ArrayList<>();
            for (int i = 1; i < records.length; i++) {
                res.add(new ImageInfo(
                        records[i].substring(starts[0], starts[1]).trim(),
                        records[i].substring(starts[1], starts[2]).trim(),
                        Integer.parseInt(records[i].substring(starts[2], starts[3]).trim()),
                        !records[i].substring(starts[3], starts[4]).trim().isEmpty(),
                        !records[i].substring(starts[4]).trim().isEmpty()
                ));
            }
            return res;
        } else {
            return null;
        }
    }

    /**
     * 下载镜像
     *
     * @param image 包含tag
     * @return
     */
    public boolean pull(String image) {
        try {
            Rule<Integer, byte[]> rule = External.runProcess("docker pull " + image);
            return rule.getKey() == 0;
        } catch (Exception e) {
            return false;
        }
    }
}
