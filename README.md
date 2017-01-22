# java-web-server
Simple pure java web server example application with a step by step tutorial on how it was created and the thoughts behind it. 
Java comes with a built in web server class that can easily be set up to serve content on URI:s.

## Step 1 - Set up a server
* We set up the Java `HttpServer` on port `8080` in the `main` method of our class `JavaWebServer`
  * This is responsible to serve the URIs we add to it on port `8080`
  * `HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);`
* Finally we give it a `null` executor for now and start our web server via `server.start();`

## Step 2 - Add our first Handler (page)
* Then we add the class `StatusHandler` that will display a simple 'Server is up' message
  * This is responsible for serving the content in a certain URI
  * It has to implement the `HttpHandler` interface
  * All our logic will be in the method `handle(HttpExchange httpExchange)`
* Then we add the `StatusHandler` to our web server on the URI '/status'
  * `server.createContext("/status", new StatusHandler());`


