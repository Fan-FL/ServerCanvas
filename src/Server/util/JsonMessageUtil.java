package Server.util;

import Server.shape.*;
import com.google.gson.Gson;

public class JsonMessageUtil {
    public static String assembleAddShapeobjectData(Shape shape){
        Gson gson = new Gson();
        String jsonObject="";
        switch (shape.classType){
            case "Pencil":
                Pencil pencil = (Pencil)shape;
                jsonObject = gson.toJson(pencil);
                break;
            case "Line":
                Line line = (Line)shape;
                jsonObject = gson.toJson(line);
                break;
            case "Rect":
                Rect rect = (Rect)shape;
                jsonObject = gson.toJson(rect);
                break;
            case "FillRect":
                FillRect fillRect = (FillRect)shape;
                jsonObject = gson.toJson(fillRect);
                break;
            case "Oval":
                Oval oval = (Oval)shape;
                jsonObject = gson.toJson(oval);
                break;
            case "FillOval":
                FillOval fillOval = (FillOval)shape;
                jsonObject = gson.toJson(fillOval);
                break;
            case "Circle":
                Circle circle = (Circle)shape;
                jsonObject = gson.toJson(circle);
                break;
            case "FillCircle":
                FillCircle fillCircle = (FillCircle)shape;
                jsonObject = gson.toJson(fillCircle);
                break;
            case "RoundRect":
                RoundRect roundRect = (RoundRect)shape;
                jsonObject = gson.toJson(roundRect);
                break;
            case "FillRoundRect":
                FillRoundRect fillRoundRect = (FillRoundRect)shape;
                jsonObject = gson.toJson(fillRoundRect);
                break;
            case "Rubber":
                Rubber rubber = (Rubber)shape;
                jsonObject = gson.toJson(rubber);
                break;
            case "Word":
                Word word = (Word)shape;
                jsonObject = gson.toJson(word);
                break;
        }

        String jsonString = "{\"cmd\":\"addShape\",\"classType\":\""+ shape.classType + "\",\"object\":"+ jsonObject + "}";
        return jsonString;
    }

    public static Shape GenerateShapeFromMessage(String classType, String objectData){
        Shape shape = null;
        Gson gson = new Gson();
        switch (classType){
            case "Pencil":
                shape = gson.fromJson(objectData, Pencil.class);
                break;
            case "Line":
                shape = gson.fromJson(objectData, Line.class);
                break;
            case "Rect":
                shape = gson.fromJson(objectData, Rect.class);
                break;
            case "FillRect":
                shape = gson.fromJson(objectData, FillRect.class);
                break;
            case "Oval":
                shape = gson.fromJson(objectData, Oval.class);
                break;
            case "FillOval":
                shape = gson.fromJson(objectData, FillOval.class);
                break;
            case "Circle":
                shape = gson.fromJson(objectData, Circle.class);
                break;
            case "FillCircle":
                shape = gson.fromJson(objectData, FillCircle.class);
                break;
            case "RoundRect":
                shape = gson.fromJson(objectData, RoundRect.class);
                break;
            case "FillRoundRect":
                shape = gson.fromJson(objectData, FillRoundRect.class);
                break;
            case "Rubber":
                shape = gson.fromJson(objectData, Rubber.class);
                break;
            case "Word":
                shape = gson.fromJson(objectData, Word.class);
                break;
        }
        return shape;
    }
}
