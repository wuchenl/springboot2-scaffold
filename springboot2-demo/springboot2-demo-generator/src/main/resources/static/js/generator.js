$(function () {
    $('#tree').treeview({
        data: getTree(),//节点数据
        onNodeSelected: function (e, o) { // 单机事件
            clickTree(o);
        }
    });
})


// function getTree() {
//     return t;
// }
var t = '[{"text": "父节点 1","nodes": [{"text": "子节点 1","nodes": [{"text": "孙子节点 1"},{"text": "孙子节点 2"}]},{"text": "子节点 2"}]},{"text": "父节点 2"},{"text": "父节点 3"},{"text": "父节点 4"},{"text": "父节点 5"}]';
// 获取数据源JSON信息
function getTree() {
    //节点上的数据遵循如下的格式：
    var tree = [{
        text: "数据源名称", //节点显示的文本值  string
        // icon: "glyphicon glyphicon-play-circle", //节点上显示的图标，支持bootstrap的图标  string
        selectedIcon: "glyphicon glyphicon-ok", //节点被选中时显示的图标       string
        // color: "#ff0000", //节点的前景色      string
        // backColor: "#1606ec", //节点的背景色      string
        href: "#http://www.baidu.com", //节点上的超链接
        selectable: true, //标记节点是否可以选择。false表示节点应该作为扩展标题，不会触发选择事件。  string
        state: { //描述节点的初始状态    Object
            checked: true, //是否选中节点
            /*disabled: true,*/ //是否禁用节点
            expanded: true, //是否展开节点
            selected: true //是否选中节点
        },
        tags: ['标签信息1', '标签信息2'], //向节点的右侧添加附加信息（类似与boostrap的徽章）    Array of Strings
        nodes: [{
            text: "t_api_day",
            id: "t_api_day"
        }, {
            text: "t_api_hour",
            id: "t_api_hour"
        }, {
            text: "t_api",
            id: "t_api"
        }, {
            text: "数据库表4"
        }]
    }];
    return tree;
}

function clickTree(o){
    console.log(o.text);
}