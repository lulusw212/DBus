package com.creditease.dbus.commons.log.processor.rule.impl;

import com.alibaba.fastjson.JSON;
import com.creditease.dbus.commons.log.processor.parse.Field;
import com.creditease.dbus.commons.log.processor.parse.RuleGrammar;
import com.creditease.dbus.commons.log.processor.rule.IRule;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by Administrator on 2017/10/12.
 */
public class SaveAsRule implements IRule {

    public List<String> transform(List<String> data, List<RuleGrammar> grammar, Rules ruleType) throws Exception{
        List<String> ret = new ArrayList<>();
        Map<Integer, List<RuleGrammar>> grammarMap = new HashMap<>();
        for (RuleGrammar rg : grammar) {
            if (StringUtils.isNoneBlank(rg.getRuleScope()) && StringUtils.isNumeric(rg.getRuleScope())) {
                if (!grammarMap.containsKey(Integer.parseInt(rg.getRuleScope()))) {
                    grammarMap.put(Integer.parseInt(rg.getRuleScope()), new ArrayList<>());
                }
                grammarMap.get(Integer.parseInt(rg.getRuleScope())).add(rg);
            }
        }
        for (int idx = 0; idx < data.size(); idx++) {
            if (grammarMap.containsKey(idx)) {
                for (RuleGrammar rg : grammarMap.get(idx)) {
                    Field field = new Field();
                    field.setEncoded(false);
                    field.setNullable(false);
                    field.setValue(idx >= data.size() ? "" : data.get(idx));
                    field.setName(rg.getName());
                    field.setType(rg.getType());
                    ret.add(JSON.toJSONString(field));
                }
            }
        }
        return ret;
    }

}
