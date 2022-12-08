

## :sparkles: Products

This class represt the product part in the application.
The class has two sides : Manager side and Clients side
- ***Manager side*** - Responsible for all products's inventory that customers can order. 
Can add new products, edit and delete existing products. Any product's changes are updated in the database.
- ***Clients side*** - Can order from the inventory of products that the manager defined in advance. 
- Each product order is according to the existing stock in the database.
---
### :bookmark_tabs: Explenation about the files:  

- ***AddNewProduct*** - Enable to the manger to add a new product.
- ***EditProduct*** - Enable to the manger to edit an exsiting product according to the details in the database.
- ***Product*** - Resresent Product object. Each product has: name, price, quantity, description and an image.
- ***ProductsClickcardActivity*** - Open a single product. For the manger it will display the options to edit and delete the products, for the client the option to order. 
- ***ProductsMainActivity*** - Display the product that is available - using recycle view.
- ***RecycleAdapter*** - An adapter for the recycle view. 
---
### :man_office_worker: Maneger side 

#### Main Page 
<img width="300" src="https://user-images.githubusercontent.com/93086649/206425464-0493f992-50bc-4cb2-ad97-eed871ed7709.jpg">


#### Click Single Product 
<img width="300" src="https://user-images.githubusercontent.com/93086649/206425450-24dcf8cb-ce3c-46aa-8f18-5add8f46b8b9.jpg">

#### Edit Product
<img width="300" src="https://user-images.githubusercontent.com/93086649/206425492-80078c6d-0af5-4bba-a76e-9536b3f786c9.jpg">

#### ADD New Product

<img width="300" src="https://user-images.githubusercontent.com/93086649/206427746-11d58584-0e0b-45be-903a-73a956931b92.jpg">

  ---

### :sassy_man: Client Side 

#### Main Page - with cart floating button 

<img width="300" src="https://user-images.githubusercontent.com/93086649/206427907-3a9b9e11-0cb2-4e95-99b1-3a050bc871e5.jpg">

#### Click Single Product 

<img width="300" src="https://user-images.githubusercontent.com/93086649/206427966-14a232cf-7e40-4cc8-b7e4-c1576470034f.jpg">


