package com.hejin.baidu;

import java.util.List;

/**
 * 作者 : 贺金龙
 * 创建时间 :  2018/2/5 18:43
 * 类描述 :
 * 修改人 :
 * 修改内容 :
 * 修改时间 :
 * 类说明 :
 *  
 */
public class WordsBean {

    /**
     * log_id : 3996615557845238481
     * direction : 3
     * words_result_num : 10
     * words_result : [{"words":"∵∵∴∵∴∵∵,∴"},{"words":".2,,"},{"words":"\u2026\u201d\u2026∴∵∴∵∵∵∵∴∵,"},{"words":"姓名贺金龙"},{"words":"性别男、民族汉"},{"words":"出生1988年9月28日"},{"words":"住址黑龙江省齐齐哈尔市铁锋"},{"words":"区木海街南山鑫苑68号楼"},{"words":"4单元203室"},{"words":"公民身份号码230203198809281011"}]
     */

    private long log_id;
    private int direction;
    private int words_result_num;
    private List<WordsResultBean> words_result;

    public long getLog_id() {
        return log_id;
    }

    public void setLog_id(long log_id) {
        this.log_id = log_id;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public int getWords_result_num() {
        return words_result_num;
    }

    public void setWords_result_num(int words_result_num) {
        this.words_result_num = words_result_num;
    }

    public List<WordsResultBean> getWords_result() {
        return words_result;
    }

    public void setWords_result(List<WordsResultBean> words_result) {
        this.words_result = words_result;
    }

    public static class WordsResultBean {
        /**
         * words : ∵∵∴∵∴∵∵,∴
         */

        private String words;

        public String getWords() {
            return words;
        }

        public void setWords(String words) {
            this.words = words;
        }
    }
}
