import React from "react";
import { toast } from 'react-toastify';

import * as api from '../../api/smartspace';
import { socketClient } from '../../api/socket';

import "./ShoppingListDisplay.css";
import MemberDisplay from './MemberDisplay';
import ShoppingItems from '../shoppingItems/ShoppingItems'
import Spinner from '../Spinner';

class ShoppingListDisplay extends React.Component {

	constructor(props) {
		super(props);
		this.state = { newMemberEmail: "", newMemberSmartspace: "" };

		this.handleNewMemberEmailChange = this.handleNewMemberEmailChange.bind(this);
		this.handleAddNewMember = this.handleAddNewMember.bind(this);
	}

	componentDidUpdate(prevProps) {
		if (this.props.user && (this.props.user !== prevProps.user)) {
			this.setState({ newMemberSmartspace: this.props.user.key.smartspace });
			socketClient.emit("joinUserRoom", this.props.user.key);
		}
	}

	handlenewMemberSmartspaceChange(event) {
		this.setState({ newMemberSmartspace: event.target.value });
	}


	handleNewMemberEmailChange(event) {
		this.setState({ newMemberEmail: event.target.value });
	}

	membersAlreadyContains(members, memberToCheck) {
		members = JSON.parse(JSON.stringify(members)); // clone

		const userToString = (userKey) => {
			return `user_${userKey.userSmartspace}_${userKey.userEmail}`;
		}

		return (members.map(member => userToString(member)).indexOf(userToString(memberToCheck)) >= 0);
	}

	handleAddNewMember() {
		let userData = this.props.user;
		let listData = this.props.shoppingListData;

		if (userData && userData.role && listData) {
			let newElementProperties = listData.elementProperties;
			newElementProperties = JSON.parse(JSON.stringify(newElementProperties)); // clone

			// add member to array
			let newUserKey = { "userSmartspace": this.state.newMemberSmartspace, "userEmail": this.state.newMemberEmail }
			newElementProperties.members.push(newUserKey);

			if (this.membersAlreadyContains(listData.elementProperties.members, newUserKey)) {
				toast.error("This user is already a member in this shopping list.");
				this.setState({ newMemberEmail: "" });
				return;
			}

			const addMember = () => {
				api.updateElement(userData.key.smartspace, userData.key.email, listData.key.smartspace, listData.key.id, { elementProperties: newElementProperties })
					.then(res => {
						toast.success("Added user " + this.state.newMemberEmail)
						listData.elementProperties.members.push({ "userSmartspace": this.state.newMemberSmartspace, "userEmail": this.state.newMemberEmail });

						this.props.updateShoppingLists();
						this.setState({ newMemberEmail: "" });
						socketClient.emit("added_new_member", {newUser: newUserKey, listName: listData.name, inviter: userData.username});
					})
					.catch(api.errorHandler);
			}

			api.getUserByKey(this.state.newMemberSmartspace, this.state.newMemberEmail)
				.then(res => addMember())
				.catch(api.errorHandler);
		}
	}

	render() {
		if (this.props.shoppingListData && !this.props.changingList) {
			let listData = this.props.shoppingListData;
			let membersDisplay = listData.elementProperties.members.map(user => {
				return <MemberDisplay user={user} key={user.userEmail} onlineUsers={this.props.onlineUsers} />
			})

			return (
				<div>
					<div className="jumbotron jumbotron-fluid py-2" style={{ backgroundColor: "rgb(220, 170, 120)" }}>
						<div className="container text-center mx-auto">
							<h1 className="display-4">{listData.name}</h1>
							<p className="lead">{listData.elementProperties.description}</p>
							<div>
								<span className="membersBox">
									{membersDisplay}
								</span>
								<span className="membersBox">
									<MemberDisplay user={{ userSmartspace: listData.creator.smartspace, userEmail: listData.creator.email }} onlineUsers={this.props.onlineUsers} />
									<span className="text-muted mx-1" data-toggle="modal" data-target="#newMember" style={{ cursor: 'pointer' }}>
										<svg xmlns="http://www.w3.org/2000/svg" width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" className="feather feather-plus-circle"><title id="title">Add Users</title><circle cx="12" cy="12" r="10"></circle><line x1="12" y1="8" x2="12" y2="16"></line><line x1="8" y1="12" x2="16" y2="12"></line></svg>
									</span>
								</span>
							</div>
						</div>
					</div>
					<div className="container">
						<ShoppingItems user={this.props.user} shoppingListData={this.props.shoppingListData} />
					</div>

					<div className="modal fade" id="newMember" tabIndex="-1" role="dialog">
						<div className="modal-dialog" role="document">
							<div className="modal-content">
								<div className="modal-header">
									<h5 className="modal-title" id="addMemberLabel">Add New Player</h5>
									<button type="button" className="close" data-dismiss="modal"></button>
								</div>
								<div className="modal-body">
									<form>
										<div className="form-group">
											<label htmlFor="list-namel" className="col-form-label">Player Smartspace:</label>
											<input type="email" className="form-control" id="list-namel" value={this.state.newMemberSmartspace} onChange={this.handlenewMemberSmartspaceChange} required />
										</div>
										<div className="form-group">
											<label htmlFor="list-namel" className="col-form-label">Player Email:</label>
											<input type="email" className="form-control" id="list-addEmail" value={this.state.newMemberEmail} onChange={this.handleNewMemberEmailChange} required />
										</div>
									</form>
								</div>
								<div className="modal-footer">
									<button type="button" className="btn btn-secondary" data-dismiss="modal">Cancel</button>
									<button type="button" className="btn btn-primary" data-dismiss="modal" onClick={this.handleAddNewMember}>Add</button>
								</div>
							</div>
						</div>
					</div>
				</div>
			);
		}

		return <div className="text-center mt-4"><h1 className="display-2 mb-4">Loading...</h1><Spinner /></div>
	}
}

export default ShoppingListDisplay;