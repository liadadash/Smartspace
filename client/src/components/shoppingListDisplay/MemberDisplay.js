import React from "react";

import * as api from '../../api/smartspace';
import "./ShoppingListDisplay.css";

class MemberDisplay extends React.Component {

	constructor(props) {
		super(props);
		this.state = { userData: null, online: false };

		this.updateUserData = this.updateUserData.bind(this);
	}

	componentDidMount() {
		this.updateUserData();
	}

	componentDidUpdate(prevProps) {
		if (this.props.user && ((this.props.user !== prevProps.user) || (this.props.onlineUsers !== prevProps.onlineUsers))) {
			this.updateUserData();
		}
	}

	updateUserData() {
		let user = this.props.user;
		let onlineUsers = this.props.onlineUsers;

		if (user) {
			api.getUserByKey(user.userSmartspace, user.userEmail)
				.then(res => this.setState({ userData: res.data }))
				.catch(api.errorHandler);

			// add green dot if online
			if (onlineUsers) {
				let isOnline = (onlineUsers.indexOf(`${user.userSmartspace}#${user.userEmail}`) >= 0);
				this.setState({ online: isOnline });
			}
		}
	}

	render() {
		if (this.state.userData) {
			let userData = this.state.userData;
			let marker = "";

			if (this.state.online) {
				marker = "online"
			}

			if (userData && userData.role === "MANAGER") {
				marker = "manager"
			}

			return (
				<span>
					<span className={marker}></span>
					<img className="rounded-circle border mx-1" src={userData.avatar} title={userData.username} alt={userData.username} width="48px" height="48px" />
				</span>
			);
		}

		return (
			<span></span>
		);
	}
}

export default MemberDisplay;
