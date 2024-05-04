package cool.zest.logicdemo.utils;

import cool.zest.logicdemo.pojo.Re.ReSentence;
import cool.zest.logicdemo.pojo.Re.Retags;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ValidatorUtil {

        // 定义所有合法的字符，包括所有大小写字母和空格
        private static final String LEGAL_CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ&|~-!.()<>[]{}AEFGXPpavZ 0123456789";

        /**
         * 检查输入字符串是否全部由合法字符组成。
         *
         * @param input 待检测的字符串
         * @return 如果字符串全部由合法字符组成，返回true；否则返回false
         */
        public static boolean isValidFormula(String input) {
            // 空字符串不是有效的公式
            if (input == null || input.trim().isEmpty()) {
                return false;
            }

            // 遍历输入字符串的每个字符
            for (int i = 0; i < input.length(); i++) {
                char c = input.charAt(i);
                // 检查字符是否为合法字符
                if (LEGAL_CHARACTERS.indexOf(c) == -1) {
                    // 如果字符不是合法字符，则返回false
                    return false;
                }
            }
            // 如果所有字符都是合法的，则返回true
            return true;
        }
    public static String parseData(String data) {
        try {
            JSONObject jsonResult = new JSONObject();
            JSONArray jsonArray = new JSONArray();

            String[] lines = data.split("\n");
            int id = 0;
            for (String line : lines) {
                JSONObject jsonObject = new JSONObject();
                String[] parts = line.split(" ");
                boolean validLine = false; // 标记当前行是否为有效行
                for (String part : parts) {
                    String[] keyValue = part.split(":");
                    if (keyValue.length == 2) {
                        String key = keyValue[0].trim();
                        String value = keyValue[1].trim();
                        if (key.equals("word")) {
                            jsonObject.put("id", String.valueOf(id));
                            jsonObject.put("word", value);
                            validLine = true; // 如果解析到了有效的 word 键值对，则将当前行标记为有效行
                        } else if (key.equals("tags")) {
                            String[] tagsArray = value.split(",");
                            JSONArray tagsJsonArray = new JSONArray();
                            for (String tag : tagsArray) {
                                tagsJsonArray.put(tag.trim());
                            }
                            jsonObject.put("tags", tagsJsonArray);
                        } else if (key.equals("success")) {
                            jsonResult.put("success", Boolean.parseBoolean(value));
                        }
                    }
                }
                if (validLine) { // 如果当前行为有效行，则将其添加到 jsonArray 中
                    jsonArray.put(jsonObject);
                    id++;
                }
            }

            jsonResult.put("result", jsonArray);

            return jsonResult.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return "false";
        }
    }
    // 检查输入字符串中是否包含时序算子
    public static boolean containsTemporalOperator(String formula) {
        // 时序算子集合
        char[] temporalOperators = {'A', 'E', 'F', 'G', 'X', 'U'};

        // 遍历字符串，检查是否包含时序算子
        for (char op : temporalOperators) {
            if (formula.contains(Character.toString(op))) {
                return true;
            }
        }

        return false;
    }
}
