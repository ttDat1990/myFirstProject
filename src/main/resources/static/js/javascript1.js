setTimeout(() => {
    const box = document.getElementById('mess');
  
    // removes element from DOM
    box.style.display = 'none';
  
    // hides element (still takes up space on page)
    // box.style.visibility = 'hidden';
  }, 2000); //time in milliseconds

//$("#mess").show().delay(3000).fadeOut();

function chooseFile(fileInput){
  if(fileInput.file && fileInput.file[0]){
      var reader = new FileReader();
      reader.onload = function(e){
          $('#image').attr('src', e.target.result);
      }
      reader.readAsDataURL(fileInput.file[0]);
  }
};

function toggle(source) {
  checkboxes = document.getElementsByName('selectMember');
  for(var i=0, n=checkboxes.length; i<n; i++) {
    checkboxes[i].checked = source.checked;
  }
}