package com.example.androidonlinequizapp.Common;

import com.example.androidonlinequizapp.Model.Question;
import com.example.androidonlinequizapp.Model.User;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;


public class Common {

    public static  String categoryId;
    public static  User currentUser;
    public  static List<Question> questionsList = new ArrayList<>();

    public static Boolean isFirebaseUser = false;

    public static FirebaseUser currentFirebaseUser;
    public static Boolean isAnonUser = false;

}
