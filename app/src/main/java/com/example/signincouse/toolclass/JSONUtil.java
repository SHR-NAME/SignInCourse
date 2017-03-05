package com.example.signincouse.toolclass;

import com.example.signincouse.model.Course;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bangzhu on 2016/8/24.
 */
public class JSONUtil {
    public static  void setValue(Course course, JSONObject obj) {
        try {
            course.setCourseID(obj.getInt("courseID"));
            course.setCourseName(obj.getString("courseName"));
            course.setCourseTeachter(obj.getString("courseTeachter"));
            course.setCourseLocation(obj.getString("courseLocation"));
            course.setCourseWeekday(obj.getInt("courseWeekday"));
            course.setScheduleId(obj.getInt("scheduleId"));
            course.setBeginCount(obj.getInt("beginCount"));
            course.setEndCount(obj.getInt("endCount"));
            course.setBeginTime(obj.getString("beginTime"));
            course.setEndTime(obj.getString("endTime"));
            if(!obj.isNull("startWeek")) {
                course.setStartWeek(obj.getInt("startWeek"));
            }
            if(!obj.isNull("endWeek")) {
                course.setEndWeek(obj.getInt("endWeek"));
            }
            course.setLocationLongitude(obj.getDouble("locationLongitude"));
            course.setLocationLatitude(obj.getDouble("locationLatitude"));
            course.setCourseCredit(obj.getInt("courseCredit"));
            if(!obj.isNull("classID")) {
                course.setClassID(obj.getInt("classID"));
            }
            course.setClassName(obj.getString("className"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public static <T> List<T> getJsonList(String src, Class<T> cls) {
        Gson gson = new Gson();
        List<T> tmpList = gson.fromJson(src, new TypeToken<List<T>>() {
        }.getType());

        if (tmpList == null)
            return null;

        List<T> list = new ArrayList<T>(tmpList.size());
        for (T map : tmpList) {
            String tmpJson = gson.toJson(map);
            list.add(gson.fromJson(tmpJson, cls));
        }
        return list;
    }

    public static <T> T getObjFromJson(String src, Class<T> cls) {
        return new Gson().fromJson(src, cls);
    }

    public static String toJSON(Object src) {
        return new Gson().toJson(src);
    }

    public static <T> String toJSONArry(List<T> list) {
        Gson gson = new Gson();
        StringBuffer json = new StringBuffer("[");
        for (T t : list) {
            json.append(gson.toJson(t));
            json.append(",");
        }
        if (json.charAt(json.length() - 1) == ',')
            json.deleteCharAt(json.length() - 1);
        json.append("]");
        return json.toString();
    }
}
