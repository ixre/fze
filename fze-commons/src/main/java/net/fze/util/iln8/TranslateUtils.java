
public class GoogleTranslateUtil {

    public String translate(String langFrom, String langTo,
                            String word) throws Exception {


        //不为空则设置代理
//        String proxyHost = PropertiesUtil.getProperty("https.proxyHost");
//        String proxyPort = PropertiesUtil.getProperty("https.proxyPort");
//        if (Objects.nonNull(proxyHost) && Objects.nonNull(proxyPort)) {
//            System.setProperty("https.proxyHost", proxyHost);
//            System.setProperty("https.proxyPort", proxyPort);
//        }


        String url = "https://translate.googleapis.com/translate_a/single?" +
                "client=gtx&" +
                "sl=" + langFrom +
                "&tl=" + langTo +
                "&dt=t&q=" + URLEncoder.encode(word, "UTF-8");

        URL obj = new URL(url);

        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        con.setRequestProperty("User-Agent", "Mozilla/5.0");

        BufferedReader in = new BufferedReader(

                new InputStreamReader(con.getInputStream()));

        String inputLine;

        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {

            response.append(inputLine);

        }

        in.close();

        return parseResult(response.toString());

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
    /**
     * 谷歌翻译 传string 反string
     */

    public String translate(String content,String lang)   {
        StringBuilder result = new StringBuilder();
        if (content.length() > 1000) {
            int size = content.length() /1000 ;
            for (int i = 0; i < size; i++) {
                String substring = content.substring(i * 1000, (i + 1) * 1000);
                try {
                    result.append(translate("zh", lang, substring));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            //for循环结束后，还有剩余的字符串
            String substring = content.substring(size * 1000, content.length());
            String translate = null;
            try {
                result.append(translate("zh", lang, substring));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }


        }else {
            try {
                result.append(translate("zh", lang, content));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        return result.toString();
    }
//
//    public static void main(String[] args) throws Exception {
//        GoogleTranslateUtil transan = new GoogleTranslateUtil();
//        //使用代理
////        GoogleTranslateUtil transan = new GoogleTranslateUtil("127.0.0.1",8080);
//
//        InetAddress myip= InetAddress.getLocalHost();
////        System.out.println("你的IP地址是："+myip.getHostAddress());
//        String translate = transan.translate("zh", "en", "卧槽！");
//        System.out.println(translate+"你的IP地址是："+myip.getHostAddress());
//    }

}


