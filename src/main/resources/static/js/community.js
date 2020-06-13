function post(){
  var parentId = $("#question_id").val();
  var content = $("#comment_content").val();
  console.log(parentId);
  console.log(content);
    comment2Target(parentId,1,content);
};
function comment2Target(targetId,type,content){
    if(!content){
        alert("回复内容不能为空");
        return
    }
    $.ajax({
        type:"POST",
        url:"/comment",
        contentType:"application/json",
        data: JSON.stringify({
            parentId:targetId,
            content : content,
            type : type
        }),
        success:function (response) {
            console.log(response);
            if(response.code == 200){
                //$("#comment_info").hide();
                //成功刷新页面
                window.location.reload();
            }else{
                if(response.code == 2003){
                    var isAccepted = confirm(response.message);
                    //isAccepted true/false
                    if(isAccepted){
                        window.open("https://github.com/login/oauth/authorize?client_id=84992e10b66d7c1c488e&redirect_uri=http://localhost:8887/callback&state=1");
                        window.localStorage.setItem("closeable",true);
                    }
                }
                alert(response.message)
            }

        },
        dataType:"json"
    })
}
function comment(e){
   var commentId = e.getAttribute("data-id");
   var content = $("#input_" + commentId).val();
   console.log(commentId+content);
   comment2Target(commentId,2,content);
}
function collapseComment(e){
    var commentId = e.getAttribute("data-id");
    //获取二级评论对象
    var collapse = $("#comment"+commentId);
    //二级评论展开之前
    collapse.on('show.bs.collapse',function(){
        var subCommentContainer = $("#comment-body"+commentId);
        if(subCommentContainer.children().length == 1){
            //先获取二级所有评论
            $.getJSON( "/comment/"+commentId, function( data ) {
                console.log("测试Jquery在页面获取后台传过来的JSON数据");
                console.log(data);

                //开始动态拼接画面，将数据动态嵌套进去
                $.each( data.data.reverse(), function(index,comment) {
                    var nameElement = $("<span/>",{
                        "html":comment.user.name
                    });
                    var gmtCreateElement = $("<span/>",{
                        "class":"float-right",
                        "html": moment(comment.gmtCreate).format("YYYY-MM-DD")
                    });
                    var h6Element = $("<h6/>",{
                        "class":"mt-0"
                    });
                    h6Element.append(nameElement);
                    var contentElement = $("<div/>",{
                        "html":comment.content
                    });
                    var dateElement = $("<div/>",{
                        "style":"color: #999999"
                    });
                    dateElement.append(gmtCreateElement);

                    var imgElement = $("<img/>",{
                        "class":"mr-3 rounded",
                        "src":comment.user.avatarUrl,
                        "width":"48"
                    });
                    var mediaElement = $("<div/>",{
                        "class":"media-body"
                    });
                    mediaElement.append(h6Element);
                    mediaElement.append(contentElement);
                    mediaElement.append(dateElement);


                    var liElement = $("<li/>",{
                        "class":"media"
                    });
                    liElement.append(imgElement);
                    liElement.append(mediaElement);

                    var ulElement = $("<ul/>",{
                        "class":"list-unstyled"
                    });
                    ulElement.append(liElement);
                    var commentElement = $("<div/>",{
                        "class":"col-lg-12 col-md-12 col-sm-12"
                    });
                    commentElement.append(ulElement);
                    subCommentContainer.prepend(commentElement);
                });
            });
        }


    })

}

/**
 * 触发显示推荐标签
 * @param e
 */
function  showTags() {
    $(".tab-panel").show();
}

/**
 * 触发隐藏推荐标签
 */
function  hideTags() {

    $(".tab-panel").hide();
}

function selectTag(e){
    var previous = $("#tag").val();
    var value = e.getAttribute("data-val");
    if(previous.indexOf(value) ==-1){
        if(previous){
            $("#tag").val(previous +","+value);
        }else{
            $("#tag").val(value);
        }
    }
}
