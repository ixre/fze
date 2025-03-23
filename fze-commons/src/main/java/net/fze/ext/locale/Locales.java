package net.fze.ext.locale;


import net.fze.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

/**
 * 本地化
 */
public class Locales {
    public static final String LANG_EN_US = "en_us";
    public static final String LANG_ZH_CN = "zh_cn";
    public static final String LANG_ZH_TW = "zh_tw";
    public static final String LANG_EN = "en";
    public static final String LANG_JP = "jp";
    public static final String LANG_KR = "kr";
    public static final String LANG_RU = "ru";
    public static final String LANG_ES = "es";
    public static final String LANG_FR = "fr";
    public static final String LANG_AR = "ar";
    public static final String LANG_ID = "id";
    public static final String LANG_VI = "vi";
    public static final String LANG_TH = "th";
    public static final String LANG_DE = "de";
    public static final String LANG_IT = "it";
    public static final String LANG_TR = "tr";
    public static final String LANG_HI = "hi";
    public static final String LANG_PT = "pt";
    public static final String LANG_EL = "el";
    public static final String LANG_NL = "nl";


    /**
     * 存放目录
     */
    private static  String LOCALE_DIR = "";
    private static final ConcurrentHashMap<String, Map<String, String>> _data = new ConcurrentHashMap<>();
    private static final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private static final int DELAY_SECONDS = 5;
    private static final Object updateLock = new Object();
    static Logger logger = LoggerFactory.getLogger(Locales.class);
    /**
     * 默认语言
     */
    private static String LANG = "en_us";
    private static volatile Future<?> updateTask;

    static {
        // 程序结束关闭定时任务
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            scheduler.shutdown();
            try {
                if (!scheduler.awaitTermination(60, TimeUnit.SECONDS)) {
                    scheduler.shutdownNow();
                }
            } catch (InterruptedException e) {
                scheduler.shutdownNow();
            }
        }));
    }

    /**
     * 设置默认语言
     *
     * @param lang 语言
     */
    public static void setDefaultLocale(String lang) {
        LANG = lang;
    }


    /**
     * 设置本地存放目录
     * @param localDir 本地存放目录
     */
    public static void setLocalDir(String localDir){
        LOCALE_DIR = localDir;
        // 延迟加载文件
        scheduler.schedule(() -> {
            try {
                loadFromFiles();
                logger.info("locale files load success");
            } catch (IOException e) {
                logger.error("Failed to load locale files", e);
            }
        }, 5, TimeUnit.SECONDS);
    }
    /**
     * 注册语言本地化
     *
     * @param lang 语言,如: zh,en
     * @param i18n 本地化文本
     */
    public static void register(String lang, I18n i18n) {
        synchronized (_data) {
            if (_data.containsKey(lang)) {
                throw new IllegalArgumentException(String.format("语言已注册:%s", lang));
            }
            _data.put(lang, i18n.getLocales());
        }
    }

    /**
     * 更新本地化文本
     *
     * @param lang  语言，如：zh,en
     * @param key   文本键
     * @param value 值
     */
    public static void update(String lang, String key, String value) {
        synchronized (_data) {
            if (!_data.containsKey(lang)) {
                _data.put(lang, new HashMap<>());
            }
            _data.get(lang).put(key, value);
            scheduleUpdate(lang);
        }
    }

    /**
     * 更新本地化文本
     *
     * @param key   语言,如:zh,en
     * @param value 值
     */
    public static void update(String key, String value) {
        update(LANG, key, value);
    }

    /**
     * 获取本地化文本
     *
     * @param lang 语言
     * @param key  键
     * @return 本地化文本
     */
    public static String get(String lang, String key) {
        if (_data.containsKey(lang)) {
            String text = _data.get(lang).get(key);
            if (!Strings.isNullOrEmpty(text)) {
                return text;
            }
        }
        return null;
    }

    /**
     * 获取本地化文本
     *
     * @param key 键
     * @return 本地化文本
     */
    public static String get(String key) {
        return get(LANG, key);
    }


    private static int _trState = -1;
    private static final GoogleTranslateUtil util = new GoogleTranslateUtil();

    /**
     * 获取本地化文本
     * @param text 文本
     * @return 翻译后的文本
     */
    public static String translate(String text){
        String value = Locales.get(text);

        if(value.equals(text)){
            // 启动翻译，并更新到本地化
            try{
                if(checkOfflineState()){
                    // 翻译不能正常工作
                    return text                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     ;
                }
                value = util.translate("zh","en",text);
                Locales.update(text,value);
            }catch (Exception e){
                System.out.println("翻译失败:"+e.getMessage());
            }
        }
        //System.out.println("翻译:"+text+"=>"+value);
        return value;
    }

    /**
     * 获取翻译后的文本
     * @param text 文本
     * @param rawLang 原始语言
     * @param toLang 目标语言
     * @return 翻译结果
     */
    public static String translate(String text,String rawLang,String toLang){
        String value = Locales.get(toLang,text);
        if(value == null){
            // 启动翻译，并更新到本地化
            try{
                if(checkOfflineState()){
                    // 翻译不能正常工作
                    return text;
                }
                value = util.translate(Locales.getFixedLang(rawLang),Locales.getFixedLang(toLang),text);
                Locales.update(toLang,text,value);
            }catch (Exception e){
                System.out.println("翻译失败:"+e.getMessage());
            }
        }
        //System.out.println("翻译:"+text+"=>"+value);
        return value;
    }

    /**
     * 检测是否支持翻译
     * @return 是/否
     */
    private static boolean checkOfflineState() {
        if(_trState == -1){
            try {
                util.translate("zh", "en", "测试");
                _trState = 1;
            }catch (Throwable ex){
                _trState = 0;
                logger.error("Failed to check translate state ,error:{}", ex.getMessage());
            }
        }
        return _trState != 1;
    }


    public static String getFixedLang(String lang) {
        if ("cn".equals(lang)) {
            return LANG_ZH_CN;
        }
        if ("tw".equals(lang) || "hk".equals(lang)) {
            return LANG_ZH_TW;
        }
        return lang;
    }

    /**
     * Save locale data to files
     *
     * @throws IOException if an I/O error occurs
     */
    private static void saveToFiles(String lang) throws IOException {
        Path dirPath = Paths.get(LOCALE_DIR);
        if (!Files.exists(dirPath)) {
            Files.createDirectories(dirPath);
        }
        Map<String, String> locales = _data.get(lang);
        Path filePath = dirPath.resolve(lang + ".txt");
        try (BufferedWriter writer = Files.newBufferedWriter(filePath, StandardCharsets.UTF_8)) {
            for (Map.Entry<String, String> locale : locales.entrySet()) {
                writer.write(locale.getKey() + "=" + locale.getValue());
                writer.newLine();
            }
        }
    }

    /**
     * Load locale data from files
     *
     * @throws IOException if an I/O error occurs
     */
    private static void loadFromFiles() throws IOException {
        Path dirPath = Paths.get(LOCALE_DIR);
        if (!Files.exists(dirPath)) {
            return;
        }

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dirPath, "*.txt")) {
            for (Path file : stream) {
                String lang = file.getFileName().toString().replace(".txt", "");
                Map<String, String> locales = new HashMap<>();

                try (BufferedReader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String[] parts = line.split("=", 2);
                        if (parts.length == 2) {
                            locales.put(parts[0], parts[1]);
                        }
                    }
                }
                _data.put(lang, locales);
            }
        }
    }

    /**
     * 延迟更新到文件，如果已存在任务，则取消上个任务
     */
    private static void scheduleUpdate(String lang) {
        if(Strings.isNullOrEmpty(LOCALE_DIR)){
            return;
        }
        synchronized (updateLock) {
            if (updateTask != null && !updateTask.isDone()) {
                updateTask.cancel(false);
            }
            updateTask = scheduler.schedule(() -> {
                try {
                    saveToFiles(lang);
                } catch (IOException e) {
                    logger.error("Failed to save locale files", e);
                }
            }, DELAY_SECONDS, TimeUnit.SECONDS);
        }
    }


    /**
     * 国际化
     */
    public interface I18n {
        /**
         * 获取本地化字典
         *
         * @return 字典
         */
        HashMap<String, String> getLocales();
    }
}
