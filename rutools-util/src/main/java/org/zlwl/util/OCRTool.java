package org.zlwl.util;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * 需要在本地安裝Tesseract-OCR
 */
public class OCRTool {

    private final String LANG_OPTION = "-l";
    private final String EOL = System.lineSeparator();

    /**
     * Tesseract-OCR的安装路径
     */
    private final String tessPath = "D://Program Files//Tesseract-OCR";
    //private String tessPath = new File("tesseract").getAbsolutePath();

    /**
     * @param imageFile 传入的图像文件
     * @return 识别后的字符串
     */
    public String recognizeText(File imageFile) throws Exception {
        /**
         * 设置输出文件的保存的文件目录
         */
        File outputFile = new File(imageFile.getParentFile(), "output");

        StringBuffer strB = new StringBuffer();
        List<String> cmd = new ArrayList<String>();

        cmd.add(tessPath + "\\tesseract");

        cmd.add("");
        cmd.add(outputFile.getName());
        cmd.add(LANG_OPTION);
//        cmd.add("eng+chi_sim");
        cmd.add("chi_sim+eng");

        ProcessBuilder pb = new ProcessBuilder();
        /**
         *Sets this process builder's working directory.
         */
        pb.directory(imageFile.getParentFile());
        cmd.set(1, imageFile.getName());
        pb.command(cmd);
        pb.redirectErrorStream(true);
        long startTime = System.currentTimeMillis();
        System.out.println("开始时间：" + startTime);
        Process process = pb.start();
        // tesseract.exe 1.jpg 1 -l chi_sim
        //不习惯使用ProcessBuilder的，也可以使用Runtime，效果一致
        // Runtime.getRuntime().exec("tesseract.exe 1.jpg 1 -l chi_sim");
        /**
         * the exit value of the process. By convention, 0 indicates normal
         * termination.
         */
//      System.out.println(cmd.toString());
        int w = process.waitFor();
        if (w == 0)// 0代表正常退出
        {
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    new FileInputStream(outputFile.getAbsolutePath() + ".txt"),
                    StandardCharsets.UTF_8));
            String str;

            while ((str = in.readLine()) != null) {
                strB.append(str).append("\n");
            }
            in.close();

            long endTime = System.currentTimeMillis();
            System.out.println("结束时间：" + endTime);
            System.out.println("耗时：" + (endTime - startTime) + "毫秒");
        } else {
            String msg = switch (w) {
                case 1 -> "Errors accessing files. There may be spaces in your image's filename.";
                case 29 -> "Cannot recognize the image or its selected region.";
                case 31 -> "Unsupported image format.";
                default -> "Errors occurred.";
            };
            throw new RuntimeException(msg);
        }
        new File(outputFile.getAbsolutePath() + ".txt").delete();
        return strB.toString();
    }
}