package com.example.samsungprojectlanglearner.data.Comp;

import java.util.LinkedList;
import java.util.List;

public class CompList {
    public static String toStr(List<Comp> compList) {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < compList.size(); i++) {
            Comp comp = compList.get(i);
            s.append(comp.getWord());
            s.append("_");
            s.append(comp.getTranslation());
            if (i != compList.size()-1) {
                s.append("__");
            }
        }
        return s.toString();
    }
    public static List<Comp> toArray(String s) {
        List<Comp> compList = new LinkedList<>();
        if (!(s.isEmpty())) {
            String[] sr = s.split("__");
            for (String str : sr) {
                String[] strComp = str.split("_");
                String strWord = strComp[0];
                String strTranslation = strComp[1];
                Comp comp = new Comp(strWord, strTranslation);
                compList.add(comp);
            }
        }
        return compList;
    }
}
