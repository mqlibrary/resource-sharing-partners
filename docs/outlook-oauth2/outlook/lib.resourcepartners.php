<html>

<head>	
	<title>Token Generation</title>
    <style>
        table,
        table tr th,
        table tr td {
            border: 1px solid black;
            border-collapse: collapse;
            padding: 5px;
            font-family: Consolas;
            font-size: 12px;
        }
        
        table tr th {
            border: 1px solid black;
            background-color: #dddd99;
            color: white;
            text-align: left;
            vertical-align: top;
            font-weight: normal;
        }

    </style>
</head>

<body>
  <h1>Authorization</h1>
  <?php  if (!array_key_exists('code', $_REQUEST)) {
    echo file_get_contents("php://input");
  } else {?>
  <table>
    <tr><th>CODE</th><td><?= @$_REQUEST['code'] ?></td></tr>
    <tr><th>STATE</th><td><?= @$_REQUEST['state'] ?></td></tr>
    <tr><th>SESSION_STATE</th><td><?= @$_REQUEST['session_state'] ?></td></tr>
  </table>
  
  <br/>
  
  <form method="POST" action="https://login.microsoftonline.com/common/oauth2/v2.0/token">
    <input type="hidden" name="client_id" value="dc958150-4b3a-7791-8496-6c07cc6f2acd" />
    <input type="hidden" name="scope" value="mail.readwrite mail.readwrite.shared offline_access" />
    <input type="hidden" name="code" value="<?= @$_REQUEST['code'] ?>" />
    <input type="hidden" name="grant_type" value="authorization_code" />
    <input type="hidden" name="redirect_uri" value="http://localhost/rspartners/lib.resourcepartners.php" />
    <input type="hidden" name="client_secret" value="agdTItSgSawSa64:|ramfouggspAQ639@%" />
    
    <input type="submit" value="Generate Token" />
  </form>  
  <?php }?>
</body>

</html>
