package net.fze.ext.locale;


import net.fze.util.OsUtils;
import net.fze.util.Strings;
import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.*;

public class GoogleTranslateUtil {
    private static final InetSocketAddress proxyAddress = new InetSocketAddress("127.0.0.1", 10808);

    public static void main(String[] args) throws Exception {
        GoogleTranslateUtil transan = new GoogleTranslateUtil();
        InetAddress myip = InetAddress.getLocalHost();
        String translate = transan.translate("zh", "en", "卧槽！");
        System.out.println(translate + "你的IP地址是：" + myip.getHostAddress());
    }

    public String translate(String langFrom, String langTo, String word) throws Exception {
        String url = "https://translate.googleapis.com/translate_a/single?" +
                "client=gtx" +
                (Strings.isNullOrEmpty(langFrom) ? "" : "&sl=" + langFrom) +
                "&tl=" + langTo +
                "&dt=t&q=" + URLEncoder.encode(word, "UTF-8");

        URL obj = new URL(url);
        HttpURLConnection con;
        if (this.isProxy()) {
            Proxy proxy = new Proxy(Proxy.Type.HTTP, proxyAddress);
            con = (HttpURLConnection) obj.openConnection(proxy);
        } else {
            con = (HttpURLConnection) obj.openConnection();
        }
        con.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; Linux x86_64; rv:136.0) Gecko/20100101 Firefox/136.0");
        con.setConnectTimeout(3000);
        con.setReadTimeout(5000);

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        StringBuffer response = new StringBuffer();
        String inputLine;

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        return parseResult(response.toString());
    }

    private boolean isProxy() {
        return OsUtils.detectPort(proxyAddress.getHostName(), proxyAddress.getPort());
    }

    private String parseResult(String inputJson) throws Exception {
        JSONArray jsonArray = new JSONArray(inputJson);
        JSONArray jsonArray2 = (JSONArray) jsonArray.get(0);
        StringBuilder result = new StringBuilder();

        for (Object o : jsonArray2) {
            result.append(((JSONArray) o).get(0).toString());
        }
        return result.toString();
    }

    public String translate(String content, String lang) {
        StringBuilder result = new StringBuilder();
        if (content.length() > 1000) {
            int size = content.length() / 1000;
            for (int i = 0; i < size; i++) {
                String substring = content.substring(i * 1000, (i + 1) * 1000);
                try {
                    result.append(translate("zh", lang, substring));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            String substring = content.substring(size * 1000);
            try {
                result.append(translate("zh", lang, substring));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                result.append(translate("zh", lang, content));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return result.toString();
    }
}