/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.yuichi_oba.ostraca.tools;

/**
 *
 * @author Yuichi-Oba
 */
public class NameConst {
	
	public  static final String HOST = "jdbc:mysql://localhost/rest?useSSL=false";
    public  static final String USER = "root";
    public  static final String PASS = "password";
	// ############################
	//	Table Name
	// ############################
	public static final String T_AFFILIATION	= "affiliation";
	public static final String T_CLASS			= "class";
	public static final String T_STUDENT		= "student";
	public static final String T_DEPARTMENT		= "department";
	public static final String T_COURSE			= "cource";
	public static final String T_TEACHER		= "teacher";
	public static final String T_TIME_TABLE		= "time_table";
	public static final String T_SUBJECT		= "subject";
	public static final String T_ATTENDLIST		= "attendlist";
	public static final String T_ROOM			= "room";
	// ############################
	//	STUDENT
	// ############################
	public static final String STU_ID			= "stu_id";
	public static final String STU_NAME			= "stu_name";
	public static final String STU_RUBI			= "stu_rubi";
	public static final String STU_MAILADDR		= "stu_mailaddr";
	// ############################
	//	TEACHER
	// ############################
	public static final String TEA_ID			= "tea_id";
	public static final String TEA_NAME			= "tea_name";
	public static final String TEA_FLG			= "tea_flg";
	public static final String TEA_MAILADDR		= "tea_mailaddr";
	// ############################
	//	DEPARTMENT
	// ############################
	public static final String DEP_ID			= "dep_id";
	public static final String DEP_NAME			= "dep_name";
	// ############################
	//	COURSE
	// ############################
	public static final String COU_ID			= "cou_id";
	public static final String COU_NAME			= "cou_name";
	// ############################
	//	SUBJECT
	// ############################
	public static final String SUB_ID			= "sub_id";
	public static final String SUB_NAME			= "sub_name";
	// ############################
	//	CLASS
	// ############################
	public static final String CLASS_ID			= "class_id";
	public static final String CLASS_NAME		= "class_name";
	// ############################
	//	ATTEND_LIST
	// ############################
	public static final String ATT_DATE			= "att_date";
	public static final String ATT_ATTEND		= "att_attend";
	public static final String ATT_PERIOD		= "att_period";
	// ############################
	//	TIME_TABLE
	// ############################
	public static final String TT_DAYOFWEEK		= "tt_dayofweek";
	public static final String TT_PERIOD		= "tt_period";
	// ############################
	//	ROOM
	// ############################
	public static final String ROOM_ID			= "room_id";

	public static final int SUBJECT_NAME = 0;
	public static final int NUM_STU_NAME = 1;
//	public static final String TEA_ID = "tea_id";
//	public static final String TEA_NAME = "tea_name";
// TODO: 2017/06/24 学校でテストする際は、要変更！ 
	public static final String IP_ADDR = "http://192.168.179.13:9000";
}