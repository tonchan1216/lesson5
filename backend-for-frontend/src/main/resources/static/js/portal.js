function addUsers(){
    var users = [];
    var form = $("#users-table input:hidden");
    for(var i = 0; i < form.length ; i++){
        users[i] = { "userId" : $(form[i]).val(),
        "firstName" : $("#firstName-" + i).val(),
        "familyName" : $("#familyName-" + i).val(),
            "address" : { "zipCode" : $("#zipCode-" + i).val(),
                "address" : $("#address-" + i).val() },
            "emails" : {"email" : $("#email-" + i + "-1").val(),
                "email" : $("#email-" + i + "-2").val()
            }
        }
    }
    var addForm = {
        "users" : users,
    };
    $.ajax({
        type : "post",
        url : $("#form").action("url"),
        data : JSON.stringify(addform),
        contentType : 'application/json',
        dataType : "json",
    }).then(
        function(data){
            displayResult(data);
        },
        function(data){
            $(".errorMessage").remove();
            $.each(data.responseJSON.messages, function(index, message){
                $("#input-folder-name").after($('<span class="errorMessage">'
                    + message
                    + '</span>'));
            });
        }
    );
}

function displayResult(data){
}

function addForm() {
    var form = $("#user-table input:hidden");
    var i = form.length;
    $("#user-table").append($('<tr id="addForm-' + i + '_user_1">' +
        '            <td rowspan="4"><input id="user-' + i + '" type="hidden">' + (i + 1) + '</td>' +
        '            <td>' +
        '              <label for="firstName-' + i + '">名前</label>' +
        '              <input id="firstName-' + i + '" type="text" name="users[' + i + '].firstName" placeholder="花子" /><br>' +
        '            </td>' +
        '            <td>' +
        '              <label for="familyName-' + i + '">苗字</label>' +
        '              <input id="familyName-' + i + '" type="text" name="users[' + i + '].familyName" placeholder="マイナビ" /><br>' +
        '            </td>' +
        '            <td rowspan="4">' +
        '              <button id="addFormButton-' + i + '" type="button" onclick="addForm()">Add form</button><br>' +
        '              <button id="deleteFormButton-' + i + '" type="button" onclick="deleteForm(' + i + ')">Delete</button>' +
        '            </td>' +
        '          </tr>' +
        '          <tr id="addForm-' + i + '_user_2">' +
        '            <td>' +
        '              <label for="loginId-' + i + '">ログインID</label>' +
        '              <input id="loginId-' + i + '" type="text" name="users[' + i + '].loginId" placeholder="hanako.mynavi" /><br>' +
        '            </td>' +
        '            <td>' +
        '              <button id="isUsableLoginIdButton-' + i + '" type="button" onclick="isUsableLoginId(' + i + ')">Usable ID?</button>' +
        '            </td>' +
        '          </tr>'+
        '          <tr id="addForm-' + i + '_address_1">' +
        '            <td>' +
        '              <label for="zipCode-' + i + '">郵便番号</label>' +
        '              <input id="zipCode-' + i + '" type="text" name="users[' + i + '].address.zipCode" placeholder="100-0000" /><br>' +
        '            </td>' +
        '            <td>' +
        '              <label for="address-' + i + '">住所</label>' +
        '              <input id="address-' + i + '" type="text" name="users[' + i + '].address.address" placeholder="東京都 港区" /><br>' +
        '            </td>' +
        '          </tr>' +
        '          <tr id="addForm-' + i + '-email_1">' +
        '            <td>' +
        '              <label for="email-' + i + '_0">Email1</label>' +
        '              <input id="email-' + i + '_0" type="text" name="users[' + i + '].emailList[0].email" placeholder="hanako.mynavi1@debugroom.org" /><br>' +
        '            </td>' +
        '        </tr>'
    ));

}

function deleteForm(userId) {
    var addForms = $('[id^="addForm-' + userId + '"]');
    for(var i = 0; i < addForms.length ; i++){
        $(addForms[i]).remove();
    }
}

function isUsableLoginId(userId) {
    var messagePanel = $("#message-panel");
    if(messagePanel != null){
        messagePanel.remove();
    }
    $.get($("#isUsableLoginIdButton-0").val() + "/isUsableLoginId?loginId=" + $("#loginId-" + userId).val(), function (data) {
        $("#loginId-" + userId)
            .after('<span id="message-panel">' + data.toString() + ' </span>');
    });

}