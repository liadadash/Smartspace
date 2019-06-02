import React from "react";
import { toast } from 'react-toastify';

import * as api from '../../api/smartspace';
import "./ShoppingListsNav.css";

class ShoppingListsNav extends React.Component {

	constructor(props) {
		super(props);
		this.state = { newListName: "", newListDesc: "" };
		this.timer = null;
		this.currentList = null;
	}

	componentDidMount() {
		this.handleNewListNameChange = this.handleNewListNameChange.bind(this);
		this.handleNewListDescChange = this.handleNewListDescChange.bind(this);
		this.handleCreateList = this.handleCreateList.bind(this);
		this.handleSelectShoppingList = this.handleSelectShoppingList.bind(this);
	}

	componentDidUpdate(prevProps) {
		if (this.props.user && (this.props.user !== prevProps.user)) {
			this.props.updateShoppingLists();
		}
	}

	handleNewListNameChange(event) {
		this.setState({ newListName: event.target.value });
	}

	handleNewListDescChange(event) {
		this.setState({ newListDesc: event.target.value });
	}

	handleSelectShoppingList(listData) {
		if (listData && this.props.shoppingListData && listData.key.smartspace === this.props.shoppingListData.key.smartspace && listData.key.id === this.props.shoppingListData.key.id) {
			return;
		}

		if (this.timer) {
			clearTimeout(this.timer);
			this.timer = null;
		}

		this.props.selectShoppingList(null);
		this.timer = setTimeout(() => {this.props.selectShoppingList(listData)}, 850);
	}

	handleCreateList() {
		if (this.props.user) {
			let smartspace = this.props.user.key.smartspace;
			let email = this.props.user.key.email;
			let listName = this.state.newListName;
			let listDesc = this.state.newListDesc;

			api.createOnlineIndicator(smartspace, email, listName).then(onlineIndicator => {
				api.createNewShoppingList(smartspace, email, listName, listDesc, onlineIndicator.data)
					.then(res => {
						toast.success("Created list " + listName);
						this.props.updateShoppingLists();
						this.setState({ newListName: "", newListDesc: "" });
						this.handleSelectShoppingList(res.data);
					})
					.catch(api.errorHandler);
			})
				.catch(api.errorHandler);
		}
	}

	render() {
		const shoppingListElements = this.props.shoppingListsData.map((shoppingList) => {
			let active = "";
			if (this.props.shoppingListData && this.props.shoppingListData.key.smartspace === shoppingList.key.smartspace && this.props.shoppingListData.key.id === shoppingList.key.id) {
				active = " active";
			}

			return <a className={"list-group-item list-group-item-action" + active} id={`list-${shoppingList.key.id}`} data-toggle="list" href={`#${shoppingList.key.id}`} key={shoppingList.key.id} role="tab" smartspace={shoppingList.key.id} onClick={this.handleSelectShoppingList.bind(null, shoppingList)} >{shoppingList.name}</a>
		});

		return (
			<React.Fragment>
				<h6 className="d-flex justify-content-between align-items-center px-3 mt-3 mb-3 text-muted">
					<span className="pl-2">My Lists</span>
					<span className="d-flex align-items-center text-muted" data-toggle="modal" data-target="#newList" style={{ cursor: 'pointer' }}>
						<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" className="feather feather-plus-circle"><circle cx="12" cy="12" r="10"></circle><line x1="12" y1="8" x2="12" y2="16"></line><line x1="8" y1="12" x2="16" y2="12"></line></svg>
					</span>
				</h6>

				<div className="list-group text-center mt-0 " id="list-tab" role="tablist">
					{shoppingListElements}
				</div>

				<div className="modal fade" id="newList" tabIndex="-1" role="dialog">
					<div className="modal-dialog" role="document">
						<div className="modal-content">
							<div className="modal-header">
								<h5 className="modal-title" id="createNewListLabel">Create A New List</h5>
								<button type="button" className="close" data-dismiss="modal"></button>
							</div>
							<div className="modal-body">
								<form>
									<div className="form-group">
										<label htmlFor="list-name" className="col-form-label">List Name:</label>
										<input type="text" className="form-control" id="list-name" value={this.state.newListName} onChange={this.handleNewListNameChange} required />
									</div>
									<div className="form-group">
										<label htmlFor="list-text" className="col-form-label">Description:</label>
										<textarea className="form-control" id="list-text" value={this.state.newListDesc} onChange={this.handleNewListDescChange}></textarea>
									</div>
								</form>
							</div>
							<div className="modal-footer">
								<button type="button" className="btn btn-secondary" data-dismiss="modal">Cancel</button>
								<button type="button" className="btn btn-primary" data-dismiss="modal" onClick={this.handleCreateList}>Create</button>
							</div>
						</div>
					</div>
				</div>
			</React.Fragment>
		);
	}
}

export default ShoppingListsNav;
