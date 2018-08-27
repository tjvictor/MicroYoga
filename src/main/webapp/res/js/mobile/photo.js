function getPhotoWallTotalCount(){
    callAjax('/websiteService/getPhotoWallTotalCount', '', 'getPhotoWallTotalCountCallback', '', '', '', '', false);
}
function getPhotoWallTotalCountCallback(data){
    if (data.status == "ok") {
        $('#photo_pagination').pagination({
            totalNumber: data.callBackData,
            pageSize: 5,
            onSelectPage: getAllPhotoWallNextPage,
        });
    }
}
function getAllPhotoWallNextPage(pageNumber, pageSize){
    getAllPhotoWallWithPhotos(pageNumber, pageSize);
    $('#photo_pagination').data('paginationObj').nextPage();
}

function getAllPhotoWallWithPhotos(pageNumber, pageSize){
    if(pageNumber===1){
        $('#photoWallView').html('');
    }
    var param = "pageNumber="+pageNumber+"&pageSize="+pageSize;
    callAjax('/websiteService/getAllPhotoWallWithPhotos', '', 'getAllPhotoWallWithPhotosCallback', '', '', param, '');
}
function getAllPhotoWallWithPhotosCallback(data){
    if (data.status == "ok" && data.callBackData.length > 0) {
        var template = '';
        for (var i = 0; i < data.callBackData.length; i++) {
            var photoWall = data.callBackData[i];
            var photoWallData = [];
            template += '<div class="page__bd div-gap-10px">';
            template += '   <div class="weui-panel weui-panel_access">';
            template += '       <div class="weui-panel__hd">';
            template += '           <div class="left auto-clip" style="width:65%">' + photoWall.name + '</div>';
            template += '           <div class="right">' + photoWall.date + '</div>';
            template += '           <div class="clear"></div>';
            template += '       </div>';
            template += '       <div style="padding:20px;">';
            template += '           <div class="weui-panel__bd">';
            template += '               <div id="' + photoWall.id + '" class="weui-grids">';

            for(var j = 0; j < photoWall.photoList.length; j++){
                if(j < 9){
                    template += '           <a class="weui-grid js_grid" style="padding:10px;"><img class="yoga-photo-browse" src="' + photoWall.photoList[j].thumbUrl + '" data-target="' + j + '" alt="" style="width:100%;height:100%" onclick="yogaPhotoBrowse(this)"></a>';
                }
                var caption = (j + 1) + " / " + photoWall.photoList.length;
                photoWallData.push({
                    "image": photoWall.photoList[j].url,
                    "caption": caption
                });
            }

            template += '               </div>';
            template += '           </div>';
            template += '       </div>';
            template += '   </div>';
            template += '</div>';
            $('#photoWallView').data(photoWall.id, photoWallData);
        }

        $('#photoWallView').html($('#photoWallView').html()+template);
    }else {
        $('#photoWallView').html('<p style="padding:0.1rem;color:red;">此墙还是空的，快来上传你美丽的照片吧。</p>');
    }
}