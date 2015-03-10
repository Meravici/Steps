package com.steps.networking;


public class Contract {	
	public static final String LOGIN_URI = "login/%s/%s";
	public static final String REGISTER_URI = "register/%s/%s/%s";
	public static final String CREATE_GROUP = "groups/new/%s/%s";
	public static final String JOIN_GROUP = "groups/add/%s/%s";
	public static final String GET_USER_GROUPS = "groups/get/user/%s";
	public static final String GET_GROUP = "groups/%s";
	public static final String GET_GROUP_USERS = "groups/users/%s";
	public static final String GET_USER_BY_NAME = "users/name/%s";
	public static final String LEAVE_GROUP = "groups/leave/%s/%s";
	public static final String GET_GROUP_TASKS = "groups/tasks/%s";
	public static final String ADD_NEW_TASK = "tasks/add/%s/%s/%s/%s/%s";
	public static final String ASSIGN_TASK = "tasks/assign/%s/%s";
	public static final String GET_TASK = "tasks/%s";
	public static final String FINISH_TASK = "tasks/finish/%s";
		
	
	
	public static class Users{
		public static final String ID = "_id";
		public static final String EMAIL = "email";
		public static final String DISPLAY_NAME = "display_name";
	}
	
	public static class Groups{
		public static final String ID = "_id";
		public static final String NAME = "name";
		public static final String ICON = "icon";
	}
	
	
	public static class Tasks{
		public static final String ID = "_id";
		public static final String TITLE = "title";
		public static final String DESCRIPTION = "description";
	    public static final String DATE_CREATED = "date_created";
	    public static final String DATE_UPDATED = "date_updated";
	    public static final String DATE_EXECUTED = "date_executed";
	    public static final String STATUS = "status";
	    public static final String USER_CREATED = "user_created";
	    public static final String USER_EXECUTED = "user_executed";
	    public static final String USER_EXECUTED_ID = "user_executed_id";
	    public static final String DATE_DUE = "date_due";
	    
	    
	    public static class STATUSES{
	    	public static final int DEFAULT = 0;
	    	public static final int TAKEN = 1;
	    	public static final int FINISHED = 2;
	    	public static final int FAILED = 3;
	    }
	}
}
