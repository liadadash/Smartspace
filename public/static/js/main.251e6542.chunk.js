(window.webpackJsonp=window.webpackJsonp||[]).push([[0],{31:function(e,t,a){},37:function(e,t,a){e.exports=a(77)},69:function(e,t,a){},72:function(e,t,a){},73:function(e,t,a){},74:function(e,t,a){},75:function(e,t,a){},77:function(e,t,a){"use strict";a.r(t);var n=a(5),s=a(6),r=a(8),i=a(7),l=a(9),c=a(0),o=a.n(c),m=a(17),u=a.n(m),h=a(15),p=a(16),d=a(2),g=a(3),b=a(32),v=a.n(b).a.create({baseURL:"http://localhost:8080/smartspace/"}),f=function(e,t){return v.get("users/login/".concat(e,"/").concat(t))},E=function(e,t,a,n,s){var r={type:e,element:{smartspace:t,id:a},player:{smartspace:n,email:s},properties:arguments.length>5&&void 0!==arguments[5]?arguments[5]:{}};return v.post("actions",r)},y=function(e,t,a,n,s,r){var i={elementType:a,name:n,latlng:{lat:s,lng:r},elementProperties:arguments.length>6&&void 0!==arguments[6]?arguments[6]:{}};return v.post("elements/".concat(e,"/").concat(t),i)},N=function(e){return o.a.createElement("div",{className:"d-flex justify-content-center"},o.a.createElement("div",{className:"spinner-border text-primary",role:"status"},o.a.createElement("span",{className:"sr-only"},"Loading...")))};N.defaultProps={message:"Loading..."};var k=N;function S(e){var t=e.user;return null!=t?o.a.createElement("div",null,o.a.createElement("div",{className:"","data-toggle":"dropdown"},o.a.createElement("img",{className:"rounded-circle p-3",src:t.avatar,alt:t.avatar,width:"64px",height:"64px"}),o.a.createElement("small",{className:"dropdown-toggle"})),o.a.createElement("div",{className:"dropdown-menu","aria-labelledby":"dropdownMenuButton"},o.a.createElement(C,{title:"View Profile",link:"./profile",icon:"info-sign"}),o.a.createElement(C,{title:"Log Out",link:"/",icon:"log-out"}))):o.a.createElement("div",{className:"p-3",style:{height:"64px"}},o.a.createElement(k,null))}function L(e){var t=e.user;return null!=t?o.a.createElement(h.b,{className:"navbar-brand mt-2 ",to:"/dashboard/".concat(t.key.smartspace,"/").concat(t.key.email,"/")},"Smartspace"):o.a.createElement("a",{className:"navbar-brand mt-2 ",href:"/"},"Smartspace")}function C(e){return o.a.createElement(h.b,{className:"dropdown-item",to:e.link},o.a.createElement("div",{className:"row"},o.a.createElement("div",{className:"col"},e.title),o.a.createElement("div",{className:"col-3 text-center"},o.a.createElement("span",{className:"glyphicon glyphicon-".concat(e.icon)}))))}var w=function(e){var t=e.user;return o.a.createElement("nav",{className:"navbar navbar-light bg-light"},o.a.createElement("div",{className:"w-100"},o.a.createElement("div",{className:"float-left"},o.a.createElement("div",{className:"dropdown ml-2"},o.a.createElement(S,{user:t}))),o.a.createElement("div",{className:"float-right"},o.a.createElement(L,{user:t}))))},O=(a(69),function(e){function t(e){var a;return Object(n.a)(this,t),(a=Object(r.a)(this,Object(i.a)(t).call(this,e))).state={newListName:"",newListDesc:""},a.timer=null,a.currentList=null,a}return Object(l.a)(t,e),Object(s.a)(t,[{key:"componentDidMount",value:function(){this.handleNewListNameChange=this.handleNewListNameChange.bind(this),this.handleNewListDescChange=this.handleNewListDescChange.bind(this),this.handleCreateList=this.handleCreateList.bind(this),this.handleSelectShoppingList=this.handleSelectShoppingList.bind(this)}},{key:"componentDidUpdate",value:function(e){this.props.user&&this.props.user!==e.user&&this.props.updateShoppingLists()}},{key:"handleNewListNameChange",value:function(e){this.setState({newListName:e.target.value})}},{key:"handleNewListDescChange",value:function(e){this.setState({newListDesc:e.target.value})}},{key:"handleSelectShoppingList",value:function(e){var t=this;e&&this.props.shoppingListData&&e.key.smartspace===this.props.shoppingListData.key.smartspace&&e.key.id===this.props.shoppingListData.key.id||(this.timer&&(clearTimeout(this.timer),this.timer=null),this.props.selectShoppingList(null),this.timer=setTimeout(function(){t.props.selectShoppingList(e)},850))}},{key:"handleCreateList",value:function(){var e=this;if(this.props.user){var t=this.props.user.key.smartspace,a=this.props.user.key.email,n=this.state.newListName,s=this.state.newListDesc;(function(e,t,a){return y(e,t,"OnlineIndicator",a+"indicator",0,0,{onlineMembers:[]})})(t,a,n).then(function(r){(function(e,t,a,n,s){return y(e,t,"ShoppingList",a,0,0,{description:n,onlineIndicator:s.key,members:[]})})(t,a,n,s,r.data).then(function(t){d.a.success("Created list "+n),e.props.updateShoppingLists(),e.setState({newListName:"",newListDesc:""}),e.handleSelectShoppingList(t.data)}).catch(function(e){e.response?d.a.error(e.response.data.message):d.a.error(e.toString())})}).catch(function(e){e.response?d.a.error(e.response.data.message):d.a.error(e.toString())})}}},{key:"render",value:function(){var e=this,t=this.props.shoppingListsData.map(function(t){var a="";return e.props.shoppingListData&&e.props.shoppingListData.key.smartspace===t.key.smartspace&&e.props.shoppingListData.key.id===t.key.id&&(a=" active"),o.a.createElement("a",{className:"list-group-item list-group-item-action"+a,id:"list-".concat(t.key.id),"data-toggle":"list",href:"#".concat(t.key.id),key:t.key.id,role:"tab",smartspace:t.key.id,onClick:e.handleSelectShoppingList.bind(null,t)},t.name)});return o.a.createElement(o.a.Fragment,null,o.a.createElement("h6",{className:"d-flex justify-content-between align-items-center px-3 mt-3 mb-3 text-muted"},o.a.createElement("span",{className:"pl-2"},"My Lists"),o.a.createElement("span",{className:"d-flex align-items-center text-muted","data-toggle":"modal","data-target":"#newList",style:{cursor:"pointer"}},o.a.createElement("svg",{xmlns:"http://www.w3.org/2000/svg",width:"24",height:"24",viewBox:"0 0 24 24",fill:"none",stroke:"currentColor",strokeWidth:"2",strokeLinecap:"round",strokeLinejoin:"round",className:"feather feather-plus-circle"},o.a.createElement("circle",{cx:"12",cy:"12",r:"10"}),o.a.createElement("line",{x1:"12",y1:"8",x2:"12",y2:"16"}),o.a.createElement("line",{x1:"8",y1:"12",x2:"16",y2:"12"})))),o.a.createElement("div",{className:"list-group text-center mt-0 ",id:"list-tab",role:"tablist"},t),o.a.createElement("div",{className:"modal fade",id:"newList",tabIndex:"-1",role:"dialog"},o.a.createElement("div",{className:"modal-dialog",role:"document"},o.a.createElement("div",{className:"modal-content"},o.a.createElement("div",{className:"modal-header"},o.a.createElement("h5",{className:"modal-title",id:"createNewListLabel"},"Create A New List"),o.a.createElement("button",{type:"button",className:"close","data-dismiss":"modal"})),o.a.createElement("div",{className:"modal-body"},o.a.createElement("form",null,o.a.createElement("div",{className:"form-group"},o.a.createElement("label",{htmlFor:"list-name",className:"col-form-label"},"List Name:"),o.a.createElement("input",{type:"text",className:"form-control",id:"list-name",value:this.state.newListName,onChange:this.handleNewListNameChange,required:!0})),o.a.createElement("div",{className:"form-group"},o.a.createElement("label",{htmlFor:"list-text",className:"col-form-label"},"Description:"),o.a.createElement("textarea",{className:"form-control",id:"list-text",value:this.state.newListDesc,onChange:this.handleNewListDescChange})))),o.a.createElement("div",{className:"modal-footer"},o.a.createElement("button",{type:"button",className:"btn btn-secondary","data-dismiss":"modal"},"Cancel"),o.a.createElement("button",{type:"button",className:"btn btn-primary","data-dismiss":"modal",onClick:this.handleCreateList},"Create"))))))}}]),t}(o.a.Component)),j=(a(31),function(e){function t(e){var a;return Object(n.a)(this,t),(a=Object(r.a)(this,Object(i.a)(t).call(this,e))).state={userData:null,online:!1},a.updateUserData=a.updateUserData.bind(Object(g.a)(a)),a}return Object(l.a)(t,e),Object(s.a)(t,[{key:"componentDidMount",value:function(){this.updateUserData()}},{key:"componentDidUpdate",value:function(e){!this.props.user||this.props.user===e.user&&this.props.onlineUsers===e.onlineUsers||this.updateUserData()}},{key:"updateUserData",value:function(){var e=this,t=this.props.user,a=this.props.onlineUsers;if(t&&(f(t.userSmartspace,t.userEmail).then(function(t){return e.setState({userData:t.data})}).catch(function(e){return d.a.error(e.toString())}),a)){var n=a.indexOf("".concat(t.userSmartspace,"#").concat(t.userEmail))>=0;this.setState({online:n})}}},{key:"render",value:function(){if(this.state.userData){var e=this.state.userData,t="";return this.state.online&&(t="online"),e&&"MANAGER"===e.role&&(t="manager"),o.a.createElement("span",null,o.a.createElement("span",{className:t}),o.a.createElement("img",{className:"rounded-circle border mx-1",src:e.avatar,title:e.username,alt:e.username,width:"48px",height:"48px"}))}return o.a.createElement("span",null)}}]),t}(o.a.Component)),x=(a(72),function(e){function t(){return Object(n.a)(this,t),Object(r.a)(this,Object(i.a)(t).apply(this,arguments))}return Object(l.a)(t,e),Object(s.a)(t,[{key:"render",value:function(){var e="",t="";return this.props.marked&&(e=" checked",t=" textChecked"),o.a.createElement("div",{className:"col-12 col-md-4 col-lg-3 mb-3"},o.a.createElement("div",{className:"mx-1 bg-light rounded border p-2 row itemBox"},o.a.createElement("div",{className:"col overflow-hidden"},o.a.createElement("span",{className:"lead itemText text-center"+t},this.props.itemName)),o.a.createElement("div",{className:"col-2 myCheckbox"+e,onClick:this.props.toggleItemMarking})))}}]),t}(o.a.Component)),D=function(e){function t(e){var a;return Object(n.a)(this,t),(a=Object(r.a)(this,Object(i.a)(t).call(this,e))).state={addItemName:"",itemsData:[]},a.handleAddItemNameChange=a.handleAddItemNameChange.bind(Object(g.a)(a)),a.handleSubmitNewItem=a.handleSubmitNewItem.bind(Object(g.a)(a)),a.toggleItemMarking=a.toggleItemMarking.bind(Object(g.a)(a)),a.updateItems=a.updateItems.bind(Object(g.a)(a)),a}return Object(l.a)(t,e),Object(s.a)(t,[{key:"componentDidUpdate",value:function(e){this.props.shoppingListData&&this.props.shoppingListData!==e.shoppingListData&&this.updateItems()}},{key:"componentDidMount",value:function(){this.updateItems()}},{key:"handleAddItemNameChange",value:function(e){this.setState({addItemName:e.target.value})}},{key:"handleSubmitNewItem",value:function(e){var t=this;if(!e.key||"Enter"===e.key){var a,n,s,r,i,l=this.props.user,c=this.props.shoppingListData;(a=l.key.smartspace,n=l.key.email,s=c.key.smartspace,r=c.key.id,i=this.state.addItemName,y(a,n,"ShoppingItem",i,0,0,{listKey:{smartspace:s,id:r},marked:!1})).then(function(e){d.a.success("Created item "+t.state.addItemName),t.updateItems(),t.setState({addItemName:""})}).catch(function(e){e.response?d.a.error(e.response.data.message):d.a.error(e.toString())})}}},{key:"updateItems",value:function(){var e=this,t=this.props.user,a=this.props.shoppingListData;(function(e,t,a,n){var s=arguments.length>4&&void 0!==arguments[4]?arguments[4]:0,r=arguments.length>5&&void 0!==arguments[5]?arguments[5]:100;return v.get("shoppingLists/".concat(e,"/").concat(t,"/").concat(a,"/").concat(n),{params:{page:s,size:r}})})(t.key.smartspace,t.key.email,a.key.smartspace,a.key.id).then(function(t){e.setState({addItemName:"",itemsData:t.data})}).catch(function(e){return d.a.error(e.toString())})}},{key:"toggleItemMarking",value:function(e){var t,a,n,s,r,i=this,l=this.props.user;(t=e.key.smartspace,a=e.key.id,n=l.key.smartspace,s=l.key.email,r=!e.elementProperties.marked,E("MarkItem",t,a,n,s,{markStatus:r})).then(function(e){return i.updateItems()}).catch(function(e){e.response?d.a.error(e.response.data.message):d.a.error(e.toString())})}},{key:"render",value:function(){var e=this,t=this.state.itemsData.map(function(t){return o.a.createElement(x,{itemName:t.name,marked:t.elementProperties.marked,toggleItemMarking:e.toggleItemMarking.bind(null,t),key:t.key.id})});return o.a.createElement("div",{className:"row"},t,o.a.createElement("div",{className:"col-12 col-md-4 col-lg-3 mb-3"},o.a.createElement("div",{className:"mx-1 bg-light rounded border p-2 row itemBox"},o.a.createElement("div",{className:"col"},o.a.createElement("input",{type:"text",className:"form-control addItemField",placeholder:"Add Item",value:this.state.addItemName,onChange:this.handleAddItemNameChange,onKeyDown:this.handleSubmitNewItem})),o.a.createElement("div",{className:"col-2 myCheckbox addIcon",onClick:this.handleSubmitNewItem}))))}}]),t}(o.a.Component),I=function(e){function t(e){var a;return Object(n.a)(this,t),(a=Object(r.a)(this,Object(i.a)(t).call(this,e))).state={newMemberEmail:"",newMemberSmartspace:""},a.handleNewMemberEmailChange=a.handleNewMemberEmailChange.bind(Object(g.a)(a)),a.handleAddNewMember=a.handleAddNewMember.bind(Object(g.a)(a)),a}return Object(l.a)(t,e),Object(s.a)(t,[{key:"componentDidUpdate",value:function(e){this.props.user&&this.props.user!==e.user&&this.setState({newMemberSmartspace:this.props.user.key.smartspace})}},{key:"handlenewMemberSmartspaceChange",value:function(e){this.setState({newMemberSmartspace:e.target.value})}},{key:"handleNewMemberEmailChange",value:function(e){this.setState({newMemberEmail:e.target.value})}},{key:"membersAlreadyContains",value:function(e,t){return(e=JSON.parse(JSON.stringify(e))).map(function(e){return JSON.stringify(e)}).indexOf(JSON.stringify(t))>=0}},{key:"handleAddNewMember",value:function(){var e=this,t=this.props.user,a=this.props.shoppingListData;if(t&&t.role&&a){var n=a.elementProperties;n=JSON.parse(JSON.stringify(n));var s={userSmartspace:this.state.newMemberSmartspace,userEmail:this.state.newMemberEmail};if(n.members.push(s),this.membersAlreadyContains(a.elementProperties.members,s))return d.a.error("This user is already a member in this shopping list."),void this.setState({newMemberEmail:""});var r=function(){(function(e,t,a,n,s){var r=s.elementName,i=s.elementType,l=s.elementProperties,c={};return null!=r&&(c.name=r),null!=i&&(c.elementType=i),null!=l&&(c.elementProperties=l),v.put("elements/".concat(e,"/").concat(t,"/").concat(a,"/").concat(n),c)})(t.key.smartspace,t.key.email,a.key.smartspace,a.key.id,{elementProperties:n}).then(function(t){d.a.success("Added user "+e.state.newMemberEmail),a.elementProperties.members.push({userSmartspace:e.state.newMemberSmartspace,userEmail:e.state.newMemberEmail}),e.props.updateShoppingLists(),e.setState({newMemberEmail:""})}).catch(function(e){e.response?d.a.error(e.response.data.message):d.a.error(e.toString())})};f(this.state.newMemberSmartspace,this.state.newMemberEmail).then(function(e){return r()}).catch(function(e){e.response?d.a.error(e.response.data.message):d.a.error(e.toString())})}}},{key:"render",value:function(){var e=this;if(this.props.shoppingListData&&!this.props.changingList){var t=this.props.shoppingListData,a=t.elementProperties.members.map(function(t){return o.a.createElement(j,{user:t,key:t.userEmail,onlineUsers:e.props.onlineUsers})});return o.a.createElement("div",null,o.a.createElement("div",{className:"jumbotron jumbotron-fluid py-2",style:{backgroundColor:"rgb(220, 170, 120)"}},o.a.createElement("div",{className:"container text-center mx-auto"},o.a.createElement("h1",{className:"display-4"},t.name),o.a.createElement("p",{className:"lead"},t.elementProperties.description),o.a.createElement("div",null,o.a.createElement("span",{className:"membersBox"},a),o.a.createElement("span",{className:"membersBox"},o.a.createElement(j,{user:{userSmartspace:t.creator.smartspace,userEmail:t.creator.email},onlineUsers:this.props.onlineUsers}),o.a.createElement("span",{className:"text-muted mx-1","data-toggle":"modal","data-target":"#newMember",style:{cursor:"pointer"}},o.a.createElement("svg",{xmlns:"http://www.w3.org/2000/svg",width:"48",height:"48",viewBox:"0 0 24 24",fill:"none",stroke:"currentColor",strokeWidth:"2",strokeLinecap:"round",strokeLinejoin:"round",className:"feather feather-plus-circle"},o.a.createElement("title",{id:"title"},"Add Users"),o.a.createElement("circle",{cx:"12",cy:"12",r:"10"}),o.a.createElement("line",{x1:"12",y1:"8",x2:"12",y2:"16"}),o.a.createElement("line",{x1:"8",y1:"12",x2:"16",y2:"12"}))))))),o.a.createElement("div",{className:"container"},o.a.createElement(D,{user:this.props.user,shoppingListData:this.props.shoppingListData})),o.a.createElement("div",{className:"modal fade",id:"newMember",tabIndex:"-1",role:"dialog"},o.a.createElement("div",{className:"modal-dialog",role:"document"},o.a.createElement("div",{className:"modal-content"},o.a.createElement("div",{className:"modal-header"},o.a.createElement("h5",{className:"modal-title",id:"addMemberLabel"},"Add New Player"),o.a.createElement("button",{type:"button",className:"close","data-dismiss":"modal"})),o.a.createElement("div",{className:"modal-body"},o.a.createElement("form",null,o.a.createElement("div",{className:"form-group"},o.a.createElement("label",{htmlFor:"list-namel",className:"col-form-label"},"Player Smartspace:"),o.a.createElement("input",{type:"email",className:"form-control",id:"list-namel",value:this.state.newMemberSmartspace,onChange:this.handlenewMemberSmartspaceChange,required:!0})),o.a.createElement("div",{className:"form-group"},o.a.createElement("label",{htmlFor:"list-namel",className:"col-form-label"},"Player Email:"),o.a.createElement("input",{type:"email",className:"form-control",id:"list-addEmail",value:this.state.newMemberEmail,onChange:this.handleNewMemberEmailChange,required:!0})))),o.a.createElement("div",{className:"modal-footer"},o.a.createElement("button",{type:"button",className:"btn btn-secondary","data-dismiss":"modal"},"Cancel"),o.a.createElement("button",{type:"button",className:"btn btn-primary","data-dismiss":"modal",onClick:this.handleAddNewMember},"Add"))))))}return o.a.createElement("div",{className:"text-center mt-4"},o.a.createElement("h1",{className:"display-2 mb-4"},"Loading..."),o.a.createElement(k,null))}}]),t}(o.a.Component),M=(a(73),function(e){function t(e){var a;return Object(n.a)(this,t),(a=Object(r.a)(this,Object(i.a)(t).call(this,e))).state={smartspace:"",email:"",selectedShoppingList:null,changingList:!1,user:null,onlineUsers:[],shoppingListsData:[]},a.selectShoppingList=a.selectShoppingList.bind(Object(g.a)(a)),a.updateOnlineMembers=a.updateOnlineMembers.bind(Object(g.a)(a)),a.updateShoppingLists=a.updateShoppingLists.bind(Object(g.a)(a)),a}return Object(l.a)(t,e),Object(s.a)(t,[{key:"componentDidMount",value:function(){var e=this;if(this.props.match&&this.props.match.params){var t=this.props.match.params;this.setState({smartspace:t.smartspace,email:t.email}),f(t.smartspace,t.email).then(function(t){return e.setState({user:t.data})}).catch(function(e){return d.a.error(e.toString())})}window.addEventListener("beforeunload",function(t){t.preventDefault(),e.state.user&&"PLAYER"===e.state.user.role&&e.checkOutThenCheckIn(e.state.selectedShoppingList,null)})}},{key:"selectShoppingList",value:function(e){null!=e?e&&this.state.selectedShoppingList&&e.key.smartspace===this.state.selectedShoppingList.key.smartspace&&e.key.id===this.state.selectedShoppingList.key.id||(this.state.user&&e&&e.elementProperties.onlineIndicator&&("PLAYER"===this.state.user.role?this.state.selectedShoppingList?this.checkOutThenCheckIn(this.state.selectedShoppingList,e):this.checkIn(e):this.updateOnlineMembers(e)),this.setState({selectedShoppingList:e,changingList:!1})):this.setState({changingList:!0})}},{key:"checkIn",value:function(e){var t,a,n,s,r=this;if(this.state.user&&e&&e.elementProperties.onlineIndicator){var i=e.elementProperties.onlineIndicator,l=this.state.user;(t=i.smartspace,a=i.id,n=l.key.smartspace,s=l.key.email,E("CheckIn",t,a,n,s)).then(function(t){r.updateOnlineMembers(e)}).catch(function(e){e.response?d.a.error(e.response.data.message):d.a.error(e.toString())})}}},{key:"checkOutThenCheckIn",value:function(e,t){var a,n,s,r,i=this;if(this.state.user&&e&&e.elementProperties.onlineIndicator){var l=e.elementProperties.onlineIndicator,c=this.state.user;(a=l.smartspace,n=l.id,s=c.key.smartspace,r=c.key.email,E("CheckOut",a,n,s,r)).then(function(e){return i.checkIn(t)}).catch(function(e){e.response?d.a.error(e.response.data.message):d.a.error(e.toString())})}}},{key:"updateOnlineMembers",value:function(e){var t,a,n,s,r=this;if(this.state.user&&e.elementProperties.onlineIndicator){var i=e.elementProperties.onlineIndicator,l=this.state.user;(t=l.key.smartspace,a=l.key.email,n=i.smartspace,s=i.id,v.get("elements/".concat(t,"/").concat(a,"/").concat(n,"/").concat(s))).then(function(e){r.setState({onlineUsers:e.data.elementProperties.onlineMembers})}).catch(function(e){return d.a.error(e.toString())})}}},{key:"updateShoppingLists",value:function(){var e=this,t=this.state.user;(function(e,t){var a=arguments.length>2&&void 0!==arguments[2]?arguments[2]:0,n=arguments.length>3&&void 0!==arguments[3]?arguments[3]:100;return v.get("shoppingLists/".concat(e,"/").concat(t),{params:{page:a,size:n}})})(t.key.smartspace,t.key.email).then(function(t){e.setState({shoppingListsData:t.data}),null==e.state.selectedShoppingList&&t.data&&t.data.length>0&&e.selectShoppingList(t.data[0])}).catch(function(e){return d.a.error(e.toString())})}},{key:"render",value:function(){return o.a.createElement("div",{className:"mainPage-display"},o.a.createElement(w,{user:this.state.user}),o.a.createElement("div",{className:"row mx-0"},o.a.createElement("div",{className:"col-2 border p-0 sidebar bg-light"},o.a.createElement(O,{user:this.state.user,selectShoppingList:this.selectShoppingList,shoppingListData:this.state.selectedShoppingList,shoppingListsData:this.state.shoppingListsData,updateShoppingLists:this.updateShoppingLists})),o.a.createElement("div",{className:"col mainContent p-0"},o.a.createElement(I,{user:this.state.user,shoppingListData:this.state.selectedShoppingList,changingList:this.state.changingList,onlineUsers:this.state.onlineUsers,updateShoppingLists:this.updateShoppingLists}))))}}]),t}(o.a.Component)),U=(a(74),function(e){function t(e){var a;return Object(n.a)(this,t),(a=Object(r.a)(this,Object(i.a)(t).call(this,e))).state={isUserLoggedIn:!1,email:"",smartspace:"2019B.nadav.peleg",user:""},a.handleEmailChange=a.handleEmailChange.bind(Object(g.a)(a)),a.handleSmartspaceChange=a.handleSmartspaceChange.bind(Object(g.a)(a)),a.handleSubmit=a.handleSubmit.bind(Object(g.a)(a)),a}return Object(l.a)(t,e),Object(s.a)(t,[{key:"handleEmailChange",value:function(e){this.setState({email:e.target.value})}},{key:"handleSmartspaceChange",value:function(e){this.setState({smartspace:e.target.value})}},{key:"handleSubmit",value:function(e){var t=this;e.preventDefault(),f(this.state.smartspace,this.state.email).then(function(e){t.setState({user:e.data,isUserLoggedIn:!0}),d.a.info("Logged in successfully")}).catch(function(e){return d.a.error(e.toString())})}},{key:"render",value:function(){return this.state.isUserLoggedIn?o.a.createElement(p.a,{to:"/dashboard/".concat(this.state.smartspace,"/").concat(this.state.email,"/")}):o.a.createElement("div",{className:"container"},o.a.createElement("div",{className:"form-signin bg-light border rounded my-5"},o.a.createElement("form",{onSubmit:this.handleSubmit},o.a.createElement("h1",{className:"h3 mb-4 text-center"},"Sign In"),o.a.createElement("input",{type:"text",className:"form-control",placeholder:"User Smartspace",value:this.state.smartspace,onChange:this.handleSmartspaceChange,required:!0}),o.a.createElement("input",{type:"email",className:"form-control mt-2",placeholder:"User Email",value:this.state.email,onChange:this.handleEmailChange,required:!0}),o.a.createElement("button",{className:"btn btn-lg btn-primary btn-block mt-3",type:"submit"},"Sign in"),o.a.createElement("hr",null),o.a.createElement("p",{className:"my-0"},o.a.createElement(h.b,{to:"/register/"}," ",o.a.createElement("small",{className:"form-text text-muted"},"Or click here to sign up")," ")))))}}]),t}(o.a.Component)),P=(a(75),function(e){function t(e){var a;return Object(n.a)(this,t),(a=Object(r.a)(this,Object(i.a)(t).call(this,e))).state={registerSuccess:!1,email:"",username:"",avatar:"",role:"PLAYER",user:null},a.handleEmailChange=a.handleEmailChange.bind(Object(g.a)(a)),a.handleUsernameChange=a.handleUsernameChange.bind(Object(g.a)(a)),a.handleAvatarChange=a.handleAvatarChange.bind(Object(g.a)(a)),a.handleRoleChange=a.handleRoleChange.bind(Object(g.a)(a)),a.handleSubmit=a.handleSubmit.bind(Object(g.a)(a)),a}return Object(l.a)(t,e),Object(s.a)(t,[{key:"handleEmailChange",value:function(e){this.setState({email:e.target.value})}},{key:"handleUsernameChange",value:function(e){this.setState({username:e.target.value})}},{key:"handleAvatarChange",value:function(e){this.setState({avatar:e.target.value})}},{key:"handleRoleChange",value:function(e){this.setState({role:e.target.value})}},{key:"handleSubmit",value:function(e){var t=this;e.preventDefault(),function(e,t,a,n){var s={email:e,role:t,username:a,avatar:n};return v.post("users",s)}(this.state.email,this.state.role,this.state.username,this.state.avatar).then(function(e){t.setState({user:e.data,registerSuccess:!0}),d.a.info("Success! You are now logged in.")}).catch(function(e){e.response?d.a.error(e.response.data.message):d.a.error(e.toString())})}},{key:"render",value:function(){if(!this.state.registerSuccess)return o.a.createElement("div",{className:"container"},o.a.createElement("div",{className:"form-signin bg-light border rounded mt-5"},o.a.createElement("form",{onSubmit:this.handleSubmit},o.a.createElement("h1",{className:"h3 mb-4 mt-2 text-center"},"Sign Up"),o.a.createElement("hr",null),o.a.createElement("input",{type:"email",className:"form-control mb-3",placeholder:"User Email",value:this.state.email,onChange:this.handleEmailChange,required:!0}),o.a.createElement("input",{type:"text",className:"form-control mb-3",placeholder:"Username",value:this.state.username,onChange:this.handleUsernameChange,required:!0}),o.a.createElement("input",{type:"text",className:"form-control mb-3",placeholder:"Avatar (image url)",value:this.state.avatar,onChange:this.handleAvatarChange,required:!0}),o.a.createElement("select",{className:"form-control",onChange:this.handleRoleChange},o.a.createElement("option",{defaultValue:!0},"PLAYER"),o.a.createElement("option",null,"MANAGER"),o.a.createElement("option",null,"ADMIN")),o.a.createElement("button",{className:"btn btn-lg btn-primary btn-block mt-3",type:"submit"},"Sign up"))));var e=this.state.user;return o.a.createElement(p.a,{to:"/dashboard/".concat(e.key.smartspace,"/").concat(e.key.email,"/")})}}]),t}(o.a.Component));function A(e){return o.a.createElement("div",{className:"card mx-auto w-50"},o.a.createElement("div",{className:"card-header"},e.title),o.a.createElement("div",{className:"card-body text-left"},e.children))}function R(e){return o.a.createElement("div",{className:"card-text form-group"},o.a.createElement("label",{htmlFor:e.id},e.label),o.a.createElement("input",{type:"text",className:"form-control",id:e.id,placeholder:e.label,value:e.value,disabled:e.disabled,onChange:e.onChange}))}var T=function(e){function t(e){var a;return Object(n.a)(this,t),(a=Object(r.a)(this,Object(i.a)(t).call(this,e))).state={username:"",avatar:""},a.handleUsernameChange=a.handleUsernameChange.bind(Object(g.a)(a)),a.handleAvatarChange=a.handleAvatarChange.bind(Object(g.a)(a)),a}return Object(l.a)(t,e),Object(s.a)(t,[{key:"componentDidUpdate",value:function(e){if(this.props.user&&this.props.user!==e.user){var t=this.props.user;this.setState({username:t.username,avatar:t.avatar})}}},{key:"handleUsernameChange",value:function(e){this.setState({username:e.target.value})}},{key:"handleAvatarChange",value:function(e){this.setState({avatar:e.target.value})}},{key:"render",value:function(){if(this.props.user){var e=this.props.user;return o.a.createElement(A,{title:"".concat(e.username,"'s Profile")},o.a.createElement("p",{className:"card-text mt-3 mb-3 lead text-center"}," ",o.a.createElement("img",{className:"rounded",src:e.avatar,alt:e.avatar,height:"100px",width:"100px"})),o.a.createElement("p",{className:"card-text mb-0 lead text-center"},"Role: ",e.role),o.a.createElement("p",{className:"card-text mt-1 lead text-center"},"Points: ",e.points),o.a.createElement("hr",null),o.a.createElement("form",{onSubmit:this.props.handleProfileUpdate},o.a.createElement(R,{id:"smartspace",label:"User Smartspace",value:e.key.smartspace,disabled:!0}),o.a.createElement(R,{id:"email",label:"User Email",value:e.key.email,disabled:!0}),o.a.createElement(R,{id:"username",label:"Username",value:this.state.username,onChange:this.handleUsernameChange}),o.a.createElement(R,{id:"avatar",label:"Avatar",value:this.state.avatar,onChange:this.handleAvatarChange}),o.a.createElement("button",{className:"btn btn-lg btn-primary btn-block mt-3",type:"submit"},"Update")))}return o.a.createElement(A,{title:"My Profile"},o.a.createElement(k,null))}}]),t}(o.a.Component),q=function(e){function t(e){var a;return Object(n.a)(this,t),(a=Object(r.a)(this,Object(i.a)(t).call(this,e))).state={smartspace:"",email:"",user:null},a.handleProfileUpdate=a.handleProfileUpdate.bind(Object(g.a)(a)),a}return Object(l.a)(t,e),Object(s.a)(t,[{key:"componentDidMount",value:function(){var e=this;if(this.props.match&&this.props.match.params){var t=this.props.match.params;this.setState({smartspace:t.smartspace,email:t.email}),f(t.smartspace,t.email).then(function(t){return e.setState({user:t.data})}).catch(function(e){return d.a.error(e.toString())})}}},{key:"handleProfileUpdate",value:function(e){var t=this;e.preventDefault();var a=e.target.username.value,n=e.target.avatar.value;(function(e,t,a){var n=a.role,s=a.username,r=a.avatar,i={};return null!=n&&(i.role=n),null!=s&&(i.username=s),null!=r&&(i.avatar=r),v.put("users/login/".concat(e,"/").concat(t),i)})(this.state.smartspace,this.state.email,{username:a,avatar:n}).then(function(){f(t.state.smartspace,t.state.email).then(function(e){t.setState({user:e.data}),d.a.success("Updated your profile!")}).catch(function(e){return d.a.error(e.toString())})})}},{key:"render",value:function(){return o.a.createElement("div",null,o.a.createElement(w,{user:this.state.user}),o.a.createElement("div",{className:"container text-center mt-5"},o.a.createElement(T,{user:this.state.user,handleProfileUpdate:this.handleProfileUpdate})))}}]),t}(o.a.Component);a(76);d.a.configure();var B=function(e){function t(){return Object(n.a)(this,t),Object(r.a)(this,Object(i.a)(t).apply(this,arguments))}return Object(l.a)(t,e),Object(s.a)(t,[{key:"componentDidMount",value:function(){}},{key:"render",value:function(){return o.a.createElement(h.a,null,o.a.createElement(p.b,{path:"/",exact:!0,component:U}),o.a.createElement(p.b,{path:"/register/",exact:!0,component:P}),o.a.createElement(p.b,{path:"/dashboard/:smartspace/:email/",exact:!0,component:M}),o.a.createElement(p.b,{path:"/dashboard/:smartspace/:email/profile/",exact:!0,component:q}))}}]),t}(o.a.Component);u.a.render(o.a.createElement(B,null),document.querySelector("#root"))}},[[37,1,2]]]);
//# sourceMappingURL=main.251e6542.chunk.js.map