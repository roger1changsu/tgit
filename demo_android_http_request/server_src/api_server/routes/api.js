var express = require('express');
var router = express.Router();

/* GET api listing. */
router.get('/', function(req, res, next) {
  var c = "undefined";
  // jsessionid
  if (undefined === req.cookies.jsessionid) {
    console.log("Can not get cookie-jsessionid from request.");
  } else {
    c = req.cookies.jsessionid;
    console.log("Request Cookie: "+c);
  }
  res.cookie('jsessionid', 'jsessionid from server');
  var resObj = {
      "key1": "request session id : "+c
  };
  // res.send(resObj);
  res.json(resObj);
});

/* POST api listing. */
router.post('/', function(req, res, next) {
  var c = "undefined";
  // jsessionid
  if (undefined === req.cookies.jsessionid) {
    console.log("Can not get cookie-jsessionid from request.");
  } else {
    c = req.cookies.jsessionid;
    console.log("Request Cookie: "+c);
  }
  res.cookie('jsessionid', 'jsessionid from server');
  var resObj = {
      "key1": "request session id : "+c
  };
  // res.send(resObj);
  res.json(resObj);
});

module.exports = router;
