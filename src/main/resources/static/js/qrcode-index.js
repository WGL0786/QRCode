function generateQRCode() {
    var text = $('#text').val();
    $.ajax({
        url: '/api/generate',
        method: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(text),
        success: function (base64Image) {
            $('#qrCodeImage').attr('src', 'data:image/png;base64,' + base64Image);
        },
        error: function () {
            alert('Error');
        }
    });
}

function decryptText() {
    var ciphertext = $('#ciphertext').val();
    $.ajax({
        url: '/api/decrypt',
        method: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(ciphertext),
        success: function (decryptedText) {
            $('#decryptedText').text(decryptedText);
        },
        error: function () {
            alert('Error');
        }
    });
}

function decodeQRCode() {
    const fileInput = document.getElementById('fileInput');
    const file = fileInput.files[0];
    const formData = new FormData();
    formData.append('file', file);
    $.ajax({
        url: '/api/decode',
        type: 'POST',
        data: formData,
        processData: false,
        contentType: false,
        // success: function (data) {
        //     将密文放入到文本框中
        //     const ciphertextInput = document.getElementById('ciphertext');
        //     ciphertextInput.value = data;
        // },
        success: function (qrCodeImageDecryptedText) {
            $('#qrCodeImageDecryptedText').text(qrCodeImageDecryptedText);
        },
        error: function () {
            alert('Failed to decode QR code image.');
        }
    });
}