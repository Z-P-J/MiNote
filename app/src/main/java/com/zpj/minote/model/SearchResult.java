package com.zpj.minote.model;

import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;

import com.zpj.minote.utils.KeywordUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchResult extends NoteItem {

    private final List<SpanPosition> spanPositionList = new ArrayList<>();

    private static class SpanPosition {
        int start;
        int end;
    }

    public static SearchResult from(JSONObject jsonObject) throws JSONException {
        SearchResult item = new SearchResult();
        JSONObject obj = jsonObject.getJSONObject("note");
        JSONArray arr = jsonObject.getJSONArray("positions");
        for (int i= 0; i < arr.length(); i++) {
            SpanPosition position = new SpanPosition();
            JSONObject o = arr.getJSONObject(i);
            position.start = o.getInt("start");
            position.end = o.getInt("stop");
            item.spanPositionList.add(position);
        }
        item.setId(obj.getString("id"));
        item.setFolderId(obj.getString("folderId"));
        item.setCreateDate(obj.getLong("createDate"));
        item.setModifyDate(obj.getLong("modifyDate"));
        item.setSnippet(obj.getString("snippet"));
        item.setStatus(obj.getString("status"));
        item.setSubject(obj.getString("subject"));
        item.setTag(obj.getString("tag"));
        item.setColorId(obj.getLong("colorId"));
        item.setAlertTag(obj.getLong("alertTag"));
        item.setAlertDate(obj.getLong("alertDate"));
        return item;
    }

}
