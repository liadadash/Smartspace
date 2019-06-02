const io = require("socket.io");
const server = io.listen(8000);

server.on("connection", (socket) => {
	console.info(`Client connected [id=${socket.id}]`);

	socket.on("joinList", (listKey) => {
		let oldListRoom = socket.lastListJoined;
		let listRoomKey = `list_${JSON.stringify(listKey)}`;

		if (oldListRoom) {
			socket.leave(oldListRoom);
			server.to(oldListRoom).emit("online_users_changed");
		}

		// send update to users in new list
		server.to(listRoomKey).emit("online_users_changed");

		// join new room
		socket.join(listRoomKey);

		// save data
		socket.lastListJoined = listRoomKey;
	});

	socket.on("joinUserRoom", (userKey) => {
		let oldUserRoom = socket.lastUserState;
		let userRoomKey = `user_${userKey.smartspace}_${userKey.email}`;

		if (oldUserRoom != userRoomKey) {
			socket.leave(oldUserRoom);
		}

		// join new room
		socket.join(userRoomKey);

		// save data
		socket.oldUserRoom = userRoomKey;

		console.log("joined user room: " + userRoomKey);
	});

	socket.on("added_new_member", (data) => {
		userKey = data.newUser;
		let userRoomKey = `user_${userKey.userSmartspace}_${userKey.userEmail}`;

		socket.to(userRoomKey).emit("update_lists");
		socket.to(userRoomKey).emit("toast_info", `${data.inviter} has added you to "${data.listName}"`);
	});

	socket.on("changed_item", (listKey) => {
		let currentListRoom = `list_${JSON.stringify(listKey)}`;
		server.to(currentListRoom).emit("list_items_changed");
	});

	socket.on("test_connection", (userKey) => {
		console.log("recived test connection");

		let userRoomKey = `user_${userKey.smartspace}_${userKey.email}`;
		socket.join(userRoomKey);

		server.to(userRoomKey).emit("connection_success");
	});

	socket.on("disconnect", () => {
		let oldListRoom = socket.lastListJoined;

		if (oldListRoom) {
			socket.leave(oldListRoom);
			setTimeout(() => {server.to(oldListRoom).emit("online_users_changed");}, 2500);
		}

		console.info(`Client gone [id=${socket.id}]`);
	});

});