<!DOCTYPE html>
<html>
<head>
    <title>Upload File with jQuery AJAX</title>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script>
        $(function () {
            $('#uploadForm').on('submit', function (e) {
                e.preventDefault();

                var formData = new FormData(this);

                $.ajax({
                    url: 'https://example.com/api/upload', // change to your endpoint
                    type: 'POST',
                    data: formData,
                    processData: false,
                    contentType: false,
                    success: function (response) {
                        console.log("Success:", response);
                        alert("Upload successful!");
                    },
                    error: function (xhr, status, error) {
                        console.error("Error:", error);
                        alert("Upload failed.");
                    }
                });
            });
        });
    </script>
</head>
<body>

    <h2>Upload File with Info (jQuery)</h2>
    <form id="uploadForm">
        <label for="name">Name:</label>
        <input type="text" name="name" id="name" required><br><br>

        <label for="file">Select File:</label>
        <input type="file" name="file" id="file" required><br><br>

        <button type="submit">Upload</button>
    </form>

</body>
</html>
