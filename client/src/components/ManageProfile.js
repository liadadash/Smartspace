import React from "react";
import { toast } from 'react-toastify';

import * as api from '../api/smartspace';
import UserDetails from "./UserDetails";
import NavBar from "./NavBar";

class ManageProfile extends React.Component {

	constructor(props) {
		super(props);
		this.state = { smartspace: "", email: "", user: null };

		this.handleProfileUpdate = this.handleProfileUpdate.bind(this);
	}

	componentDidMount() {
		if (this.props.match && this.props.match.params) {

			let params = this.props.match.params;
			this.setState({ smartspace: params.smartspace, email: params.email });

			api.getUserByKey(params.smartspace, params.email)
				.then(res => this.setState({ user: res.data }))
				.catch(api.errorHandler);
		}
	}

	handleProfileUpdate(event) {
		event.preventDefault();

		let newUsername = event.target.username.value;
		let newAvatar = event.target.avatar.value;

		const updateUserData = () => {
			api.getUserByKey(this.state.smartspace, this.state.email).then(res => {
				this.setState({ user: res.data });
				toast.success("Updated your profile!");
			}
			).catch(api.errorHandler);
		}

		api.updateUser(this.state.smartspace, this.state.email, { username: newUsername, avatar: newAvatar }).then(updateUserData);
	}

	render() {
		return (
			<div>
				<NavBar user={this.state.user} />
				<div className="container text-center mt-5">
					<UserDetails user={this.state.user} handleProfileUpdate={this.handleProfileUpdate} />
				</div>
			</div>
		);
	}
}

export default ManageProfile;
