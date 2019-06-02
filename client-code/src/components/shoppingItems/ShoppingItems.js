import React from "react";
import { toast } from 'react-toastify';

import * as api from '../../api/smartspace';
import {socketClient} from '../../api/socket';

import "./ShoppingItems.css";

// TODO: change to functinal componenet
class Item extends React.Component {

	render() {
		let isMarked = "";
		let textMarked = "";

		if (this.props.marked) {
			isMarked = " checked"
			textMarked = " textChecked"
		}

		return (
			<div className="col-12 col-md-4 col-lg-3 mb-3">
				<div className="mx-1 bg-light rounded border p-2 row itemBox">
					<div className="col overflow-hidden">
						<span className={"lead itemText text-center" + textMarked}>{this.props.itemName}</span>
					</div>
					<div className={"col-2 myCheckbox" + isMarked} onClick={this.props.toggleItemMarking}></div>
				</div>
			</div>
		);
	}
}

class ShoppingItems extends React.Component {

	constructor(props) {
		super(props);
		this.state = { addItemName: "", itemsData: [] };

		this.handleAddItemNameChange = this.handleAddItemNameChange.bind(this);
		this.handleSubmitNewItem = this.handleSubmitNewItem.bind(this);
		this.toggleItemMarking = this.toggleItemMarking.bind(this);
		this.updateItems = this.updateItems.bind(this);
	}

	componentDidUpdate(prevProps) {
		if (this.props.shoppingListData && (this.props.shoppingListData !== prevProps.shoppingListData)) {
			this.updateItems();
		}
	}

	componentDidMount() {
		this.updateItems();

		socketClient.on("list_items_changed", this.updateItems);
	}

	componentWillUnmount() {
		socketClient.off("list_items_changed", this.updateItems);
	}

	handleAddItemNameChange(event) {
		this.setState({ addItemName: event.target.value });
	}

	handleSubmitNewItem(e) {
		// submit when pressing enter
		if (e.key && e.key !== 'Enter') {
			return;
		}

		let user = this.props.user;
		let listData = this.props.shoppingListData;

		api.createNewShoppingItem(user.key.smartspace, user.key.email, listData.key.smartspace, listData.key.id, this.state.addItemName)
			.then(res => {
				toast.success("Created item " + this.state.addItemName);

				this.updateItems();
				socketClient.emit("changed_item", listData.key);
				this.setState({ addItemName: "" });
			})
			.catch(api.errorHandler);
	}

	updateItems() {
		let user = this.props.user;
		let listData = this.props.shoppingListData;

		api.getAllItemsByShoppingList(user.key.smartspace, user.key.email, listData.key.smartspace, listData.key.id)
			.then((res) => {
				this.setState({ addItemName: "", itemsData: res.data });
			})
			.catch(api.errorHandler);
	}

	toggleItemMarking(itemData) {
		let user = this.props.user;

		api.invokeMarkItem(itemData.key.smartspace, itemData.key.id, user.key.smartspace, user.key.email, !itemData.elementProperties.marked)
			.then(res => {
				this.updateItems();
				socketClient.emit("changed_item", this.props.shoppingListData.key);
			})
			.catch(api.errorHandler);
	}

	render() {
		let items = this.state.itemsData.map(item => <Item itemName={item.name} marked={item.elementProperties.marked} toggleItemMarking={this.toggleItemMarking.bind(null, item)} key={item.key.id} />)

		return (
			<div className="row">
				{items}

				<div className="col-12 col-md-4 col-lg-3 mb-3">
					<div className="mx-1 bg-light rounded border p-2 row itemBox">
						<div className="col">
							<input type="text" className="form-control addItemField text-center" placeholder="Add Item" value={this.state.addItemName} onChange={this.handleAddItemNameChange} onKeyDown={this.handleSubmitNewItem} />
						</div>
						<div className="col-2 myCheckbox addIcon" onClick={this.handleSubmitNewItem}></div>
					</div>
				</div>

			</div>
		);
	}
}

export default ShoppingItems;
