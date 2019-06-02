import React from "react";

import * as api from '../../api/smartspace';
import {socketClient} from '../../api/socket';

import NavBar from "../NavBar";
import ShoppingListsNav from "../shoppingListsNav/ShoppingListsNav";
import ShoppingListDisplay from "../shoppingListDisplay/ShoppingListDisplay";

import "./MainPage.css";

class MainPage extends React.Component {

	constructor(props) {
		super(props);
		this.state = { smartspace: "", email: "", selectedShoppingList: null, changingList: false, user: null, onlineUsers: [], shoppingListsData: [] };

		this.selectShoppingList = this.selectShoppingList.bind(this);
		this.updateOnlineMembers = this.updateOnlineMembers.bind(this);
		this.updateShoppingLists = this.updateShoppingLists.bind(this);
	}

	componentDidMount() {
		if (this.props.match && this.props.match.params) {
			let params = this.props.match.params;
			this.setState({ smartspace: params.smartspace, email: params.email });
			api.getUserByKey(params.smartspace, params.email)
				.then(res => this.setState({ user: res.data }))
				.catch(api.errorHandler);
		}

		window.addEventListener("beforeunload", (ev) => {
			ev.preventDefault();
			if (this.state.user && this.state.user.role === "PLAYER") {
				this.checkOutThenCheckIn(this.state.selectedShoppingList, null);
			}
		});

		socketClient.on("userCheckIn", data => {console.log(data)});
		socketClient.on("online_users_changed", data => {this.updateOnlineMembers(this.state.selectedShoppingList)});
		socketClient.on("update_lists", this.updateShoppingLists);
	}

	componentWillUnmount() {
		// maybe do logout / my profile check out here?? 
	}

	selectShoppingList(newListData) {
		// show loading
		if (newListData == null) {
			this.setState({ changingList: true });
			return;
		}

		// return if change to same list
		if (newListData && this.state.selectedShoppingList && newListData.key.smartspace === this.state.selectedShoppingList.key.smartspace && newListData.key.id === this.state.selectedShoppingList.key.id) {
			return;
		}

		// check to new list and check out from last list
		if (this.state.user && newListData && newListData.elementProperties.onlineIndicator) {
			if (this.state.user.role === "PLAYER") {
				if (this.state.selectedShoppingList) {
					this.checkOutThenCheckIn(this.state.selectedShoppingList, newListData) // check out from old list then check in to new list.
				} else {
					this.checkIn(newListData); // only check in to new list.
				}
			} else {
				this.updateOnlineMembers(newListData); // manager can't check in, only get online status.
				socketClient.emit("joinList", newListData.key);
			}
		}

		// change selected list
		this.setState({ selectedShoppingList: newListData, changingList: false });
	}

	checkIn(newListData) {
		if (this.state.user && newListData && newListData.elementProperties.onlineIndicator) {
			let onlineIndicator = newListData.elementProperties.onlineIndicator;
			let user = this.state.user;

			api.invokeCheckIn(onlineIndicator.smartspace, onlineIndicator.id, user.key.smartspace, user.key.email)
				.then(res => { 
					this.updateOnlineMembers(newListData);
					socketClient.emit("joinList", newListData.key);
				})
				.catch(api.errorHandler);
		}
	}

	checkOutThenCheckIn(oldListData, newListData) {
		if (this.state.user && oldListData && oldListData.elementProperties.onlineIndicator) {
			let onlineIndicator = oldListData.elementProperties.onlineIndicator;
			let user = this.state.user;

			api.invokeCheckOut(onlineIndicator.smartspace, onlineIndicator.id, user.key.smartspace, user.key.email)
				.then(res => this.checkIn(newListData))
				.catch(api.errorHandler);
		}
	}

	updateOnlineMembers(newListData) {
		if (this.state.user && newListData.elementProperties.onlineIndicator) {
			let onlineIndicator = newListData.elementProperties.onlineIndicator;
			let user = this.state.user;

			api.getElementByKey(user.key.smartspace, user.key.email, onlineIndicator.smartspace, onlineIndicator.id)
				.then(res => {
					this.setState({ onlineUsers: res.data.elementProperties.onlineMembers })
				})
				.catch(api.errorHandler);
		}
	}

	updateShoppingLists() {
		let user = this.state.user;

		api.getAllShoppingLists(user.key.smartspace, user.key.email)
			.then((res) => {
				this.setState({ shoppingListsData: res.data });

				if (this.state.selectedShoppingList == null && res.data && res.data.length > 0) {
					this.selectShoppingList(res.data[0]);
				}
			})
			.catch(api.errorHandler);
	}

	render() {
		return (
			<div className='mainPage-display'>
				<NavBar user={this.state.user} />
				<div className="row mx-0">
					<div className="col-2 border p-0 sidebar bg-light">
						<ShoppingListsNav user={this.state.user} selectShoppingList={this.selectShoppingList} shoppingListData={this.state.selectedShoppingList} shoppingListsData={this.state.shoppingListsData} updateShoppingLists={this.updateShoppingLists} />
					</div>
					<div className="col mainContent p-0">
						<ShoppingListDisplay user={this.state.user} shoppingListData={this.state.selectedShoppingList} changingList={this.state.changingList} onlineUsers={this.state.onlineUsers} updateShoppingLists={this.updateShoppingLists} />
					</div>
				</div>
			</div>
		);
	}
}

export default MainPage;
