package com.steps.networking;

import android.database.Cursor;

public class ServerAPI {
	private static ServerAPI instance;
	private ServerAPI(){
		
	}
	
	public static ServerAPI getInstance(){
		if (instance == null) {
			// Thread Safe. Might be costly operation in some case
			synchronized (ServerAPI.class) {
				if (instance == null) {
					instance = new ServerAPI();
				}
			}
		}
		return instance;
	}

	public void login(String email, String password,
			OnFinishListener onFinish) {
		new CursorAsyncTask(
				URIFactory.getLoginURI(email, password),
				new String[] { Contract.Users.ID, Contract.Users.DISPLAY_NAME },
				onFinish).execute();
	}

	public void register(String email, String password,
			String displayName, OnFinishListener onFinish) {

		new CursorAsyncTask(URIFactory.getRegisterURI(email, password,
				displayName), new String[] { Contract.Users.ID,
				Contract.Users.DISPLAY_NAME }, onFinish).execute();
	}

	public void getUserGroups(int userID,
			OnFinishListener onFinishListener) {
		new CursorAsyncTask(URIFactory.getUserGroupsURI(userID), new String[] {
				Contract.Groups.ID, Contract.Groups.NAME, Contract.Groups.ICON },
				onFinishListener).execute();

	}

	public void createGroup(int icon, final int userID, String name,
			final OnFinishListener onFinishListener) {
		new CursorAsyncTask(URIFactory.getCreateGroupURI(name, icon),
				new String[] { Contract.Groups.ID }, new OnFinishListener() {

					@Override
					public void OnFinish(Cursor result, ErrorCode err) {
						result.moveToFirst();
						int groupID = result.getInt(0);
						joinGroup(userID, groupID, onFinishListener);
					}
				}).execute();
	}

	public void joinGroup(int userID, int groupID,
			OnFinishListener onFinishListener) {
		new CursorAsyncTask(URIFactory.getJoinGroupURI(userID, groupID),
				new String[] { Contract.Users.ID }, onFinishListener).execute();
	}

	public void getGroup(int groupID, OnFinishListener onFinishListener) {
		new CursorAsyncTask(URIFactory.getGroupURI(groupID),
				new String[] { Contract.Groups.ID, Contract.Groups.NAME,
						Contract.Groups.ICON }, onFinishListener).execute();
	}

	public void getGroupUsers(int groupID,
			OnFinishListener onFinishListener) {
		new CursorAsyncTask(URIFactory.getGroupUsersURI(groupID), new String[] {
				Contract.Users.ID, Contract.Users.DISPLAY_NAME },
				onFinishListener).execute();
	}

	public void getUserByName(String name,
			OnFinishListener onFinishListener) {
		new CursorAsyncTask(URIFactory.getUserByName(name), new String[] {
				Contract.Users.ID, Contract.Users.DISPLAY_NAME },
				onFinishListener).execute();
	}

	public void leaveGroup(int groupID, int userID,
			OnFinishListener onFinishListener) {
		new CursorAsyncTask(URIFactory.getLeaveGroupURI(groupID, userID), null,
				onFinishListener).execute();

	}

	public void getGroupTasks(int id, OnFinishListener onFinishListener) {
		new CursorAsyncTask(URIFactory.getGroupTasks(id), new String[] {
				Contract.Tasks.STATUS, Contract.Tasks.ID, Contract.Tasks.TITLE,
				Contract.Tasks.DATE_DUE }, onFinishListener).execute();

	}

	public void addNewTask(int groupID, String title,
			String description, String dateDue, int userID,
			OnFinishListener onFinishListener) {
		new CursorAsyncTask(URIFactory.getAddTaskURI(groupID, title,
				description, dateDue, userID), null, onFinishListener)
				.execute();
	}
	
	
	public void assignTask(int taskID, int userID, OnFinishListener onFinishListener){
		new CursorAsyncTask(URIFactory.getAssignTaskURI(taskID, userID), null, onFinishListener).execute();
	}
	
	public void getTask(int taskID, OnFinishListener onFinishListener){
		new CursorAsyncTask(
				URIFactory.getTaskURI(taskID), 
				new String[]{
					Contract.Tasks.ID,
					Contract.Tasks.TITLE,
					Contract.Tasks.DESCRIPTION,
					Contract.Tasks.DATE_CREATED,
					Contract.Tasks.DATE_UPDATED,
					Contract.Tasks.DATE_EXECUTED,
					Contract.Tasks.DATE_DUE,
					Contract.Tasks.STATUS,
					Contract.Tasks.USER_CREATED,
					Contract.Tasks.USER_EXECUTED,
					Contract.Tasks.USER_EXECUTED_ID
				}, 
				onFinishListener).execute();
	}

	public void finishTask(int taskID, OnFinishListener onFinishListener) {
		new CursorAsyncTask(URIFactory.getFinishTaskURI(taskID), null, onFinishListener).execute();
		
	}

}