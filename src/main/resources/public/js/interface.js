/**
 * Created by boshu2 on 2015/12/23.
 */
function getProjectList()
{
    $.getJSON('/project/getAllProjects',function(data){
        //$("#ul-project-list").siblings.remove();
        $.each(data, function(i, item) {
            $("#project-list").append(
                "<ul class='nav nav-sidebar'>"+
                  "<li><a href='project/"+item.id+"/getAllModules' class='nav-text'><img src='/image/icon-project"+item.id+".png'>"+item.name+"</a></li>"+
                "</ul>");
        });
    });
}