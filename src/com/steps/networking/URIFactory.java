package com.steps.networking;

import java.util.Formatter;

import android.net.Uri;


public class URIFactory{
	
	
	public static Uri getLoginURI(String email, String password){
		return getUri(Contract.LOGIN_URI, email, password);
	}
	
	public static Uri getRegisterURI(String email, String password, String displayName){
		return getUri(Contract.REGISTER_URI, email, password, displayName);
	}
	
	public static Uri getCreateGroupURI(String name, int icon){
		return getUri(Contract.CREATE_GROUP, name, Integer.toString(icon));
	}
	
	public static Uri getJoinGroupURI(int userID, int groupID){
		return getUri(Contract.JOIN_GROUP, Integer.toString(userID), Integer.toString(groupID));
	}
	
	public static Uri getUserGroupsURI(int userID){
		return getUri(Contract.GET_USER_GROUPS, Integer.toString(userID));
	}
	
	public static Uri getGroupURI(int groupID) {
		return getUri(Contract.GET_GROUP, Integer.toString(groupID));
	}
	
	public static Uri getGroupUsersURI(int groupID) {
		return getUri(Contract.GET_GROUP_USERS, Integer.toString(groupID));
	}
	
	
	
	
	private static Uri getUri(String base, String ... parameters){
		Formatter formatter = new Formatter();
		formatter.format(base, (Object[])encode(parameters));
		String formatted = formatter.toString();
		formatter.close();
		return Uri.parse(formatted);
	}
	
	private static String[] encode(String[] parameters){
		String[] res = new String[parameters.length];
		for(int i=0; i<parameters.length; i++)
			res[i] = Uri.encode(parameters[i]);
		return res;
	}

	public static Uri getUserByName(String name) {
		return getUri(Contract.GET_USER_BY_NAME, name);
	}

	public static Uri getLeaveGroupURI(int groupID, int userID) {
		return getUri(Contract.LEAVE_GROUP, Integer.toString(groupID), Integer.toString(userID));
	}

	public static Uri getGroupTasks(int id) {
		return getUri(Contract.GET_GROUP_TASKS, Integer.toString(id));
	}
	
	public static Uri getAddTaskURI(int groupID, String title, String description, String dateDue, int userID){
		return getUri(Contract.ADD_NEW_TASK, Integer.toString(groupID), title, description, dateDue, Integer.toString(userID));
	}

	public static Uri getAssignTaskURI(int taskID, int userID) {
		return getUri(Contract.ASSIGN_TASK, Integer.toString(taskID), Integer.toString(userID));
	}

	public static Uri getTaskURI(int taskID) {
		return getUri(Contract.GET_TASK, Integer.toString(taskID));
	}

	public static Uri getFinishTaskURI(int taskID) {
		return getUri(Contract.FINISH_TASK, Integer.toString(taskID));
	}
}
