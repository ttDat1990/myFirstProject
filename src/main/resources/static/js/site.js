//auto hide for message
setTimeout(() => {
    const box = document.getElementById('mess');
    const box1 = document.getElementById('mess1');
  
    // removes element from DOM
    box.style.display = 'none';
    box1.style.display = 'none';
  
    // hides element (still takes up space on page)
    // box.style.visibility = 'hidden';
  }, 2000); //time in milliseconds

//$("#mess").show().delay(3000).fadeOut();

//change att src for img when onchange input tag
function chooseFile(fileInput){
  if(fileInput.files && fileInput.files[0]){
      var reader = new FileReader();
      reader.onload = function(e){
          $('#image').attr('src', e.target.result);
      }
      reader.readAsDataURL(fileInput.files[0]);
  }
};

// Check all check box
function toggle(source) {
  checkboxes = document.getElementsByName('selectMember');
  for(var i=0, n=checkboxes.length; i<n; i++) {
    checkboxes[i].checked = source.checked;
  }
}

// Display modal confirm for delete product
function showConfirmModalDialog(id, name){
  $('#productName').text(name);
  $('#yesOption').attr('href', '/admin/products/delete/' + id);
  $('#confirmationId').modal('show');
}

// Display modal confirm for delete category
function showConfirmModalDialog1(id, name){
  $('#categoryName').text(name);
  $('#yesOption').attr('href', '/admin/categories/delete/' + id);
  $('#confirmationId').modal('show');
}

// Display modal confirm for delete account
// function showConfirmModalDialog2(id, name){
//   $('#accountuser').text(name);
//   $('#yesOption').attr('href', '/admin/accounts/delete/' + id);
//   $('#confirmationId').modal('show');
// }