# java-web-server
Simple pure java web server example application with a step by step tutorial on how it was created and the thoughts behind it. 
Java comes with a built in web server class that can easily be set up to serve content on URI:s.

## Step 1 - Set up a server
* We set up the Java `HttpServer` on port `8000` in the `main` method of our class `JavaWebServer`
  * This is responsible to serve the URIs we add to it on port `8000`
  * `HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);`
* Finally we give it a `null` executor for now and start our web server via `server.start();`

## Step 2 - Add our first Handler (page)
* Then we add the class `StatusHandler` that will display a simple 'Server is up' message
  * This is responsible for serving the content in a certain URI
  * It has to implement the `HttpHandler` interface
  * All our logic will be in the method 
  ```
  String response = "Server is up";
  httpExchange.sendResponseHeaders(200, response.length());
  OutputStream os = httpExchange.getResponseBody();
  os.write(response.getBytes());
  os.close();
* Then we add the `StatusHandler` to our web server on the URI '/status'
  * `server.createContext("/status", new StatusHandler());`

## Step 3 - Add a greeting Handler
* We add the class `GreetingHandler` with the simple text 'Greetings!'
  * This is set up the exact same way as the `StatusHandler` was. Same content in the class itself 
  and registered to the `/` address

Now we can see that there is a lot of overlap in logic in the two handlers. 
To make this more efficient we are going to extract the overlapping lines to a helper class

## Step 4 - Extract overlapping logic to a Helper class
* Create a package named `helper` next to the `handler` package and create the class `HandlerHelper`
* Create a `static void` method in `HandlerHelper` named `writeReponse` that take a `String` and an `HttpExchange`
  * `public static void writeResponse(String response, HttpExchange httpExchange) throws IOException {`
* Take the overlapping lines from the `StatusHandler` and put it in `HandlerHelper`
  ```
  httpExchange.sendResponseHeaders(200, response.length());
  OutputStream os = httpExchange.getResponseBody();
  os.write(response.getBytes());
  os.close();
* Change the `StatusHandler` and `GreetingHandler` to use the helper
  * Replace everything except for `String response = "Server is up";` with `HandlerHelper.writeResponse(response, httpExchange);`

  