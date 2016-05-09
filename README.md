IQrypt SDK is an encryption library that can be embedded into Mobile or IoT applications and its goal is to encrypt data that will be stored in a cloud database. This version provides the SDK for Android, to compile it, open IQryptSDK project with Android Studio version 2.1 and Build module IQryptSDK. There are also tests that can be found in ApplicationTest and can be run and inspected to see how IQrypt works.

IQrypt works best with [Document Oriented Databases](https://en.wikipedia.org/wiki/Document-oriented_database) like [MongoDB](https://www.mongodb.org/) or [Couchbase](http://www.couchbase.com/), but it can be used and adapted also for other cloud databases like [SQL Azure](https://azure.microsoft.com/en-us/services/sql-database/).

It encrypts the JSON records(documents) on the device(at the client side), before they are stored locally or transferred to the cloud. The data stays encrypted in the cloud and thanks to our technology the server can execute **queries over encrypted data without decrypting it**. 

More info about use cases and benefits can be found on [iqrypt.com](http://iqrypt.com/).

This solution contains IQrypt standalone SDK that can be used to further integrate with other frameworks and database SDKs. A full sample can be found in samples\IQrypt_CouchbaseLite_Sample\ where it shows how you can integrate IQrypt with CouchbaseLite which may be sync with a cloud Couchbase Server.

Usually JSON Documents are stored in Collections, a collection can be associated with Tables in relational databases. Every document consists in a list of key-value pairs, the values can be also another list of key-value pairs, arrays or simple types. Within every document it is embedded a special field called “Id” - unique identifier within a collection. An important note here is that every document may have different schema, it may have different fields, so the data model is very flexible.
To obtain the desired security level, we will modify this data model, so our Document will have the following model:

* Id – unique identifier

* Content – document content as described above and where resides all data.

* Tags – a simple key-value pair list

Our process flow will be as following: before encryption, we will extract the keywords from the Document content that we want to search by and put them as Tags. So in fact, we will store some data redundantly, once inside Document content and once within the Tags. 
The encryption will be done as follow:
* Id – will be let plain text because usually should not reveal anything sensitive.

* Content will be encrypted with a semantically - secure encryption scheme, we will call it further RND.

* Each tag value will be encrypted with different encryption scheme based on the search needs as follow: 

    * HMAC – keyed-hash message authentication code used for equality/non-equalities queries 
    * DET – deterministic encryption used for equality /non-equality queries
    * OPE – deterministic order preserving encryption used for equality/non-equlity queries and also for range queries
    * ROPE – randomized order preserving encryption used for equality/non-equlity queries and also for range queries
    * BITMAP – client side encrypted bitmap indexes used for low cardinality tags capable to sustain equality/non-equlity queries and also range queries
    * PLAINTEXT – plain text, no encryption, all types of queries

Using combinations of these encryption schemes, IQrypt will allow you to store data highly secure and at the same time allow server to execute queries without decrypting the data.

## Randomized encryption scheme (RND)

Document content is encrypted with this scheme.This scheme uses AES and/or Camellia as block ciphers with a key size of 128 and/or 256 bits and as mode of operation we use CBC with a random initial vector (IV).Such scheme it is considered semantically-secure providing indistinguishability under chosen plaintext attack (IND-CPA).

## Keyed-hash scheme (HMAC)

HMAC – keyed-hash authentication it is one-way function, it means once you apply this function over a plain text you cannot reverse it. For this scheme we use SHA-256 as hashing algorithm. 
This construction allows us to make equality/non-equality queries for Tag values. Another important feature of this function is the fact that for any input size, the output has always the same size (256 bits in our case), so you can combine, for example, multiple fields values from the Document and put it as one Tag. Decryption of this tag value is not needed in our case, because we have the actual value within the document anyway, so using such a query, the server will return us the matched documents and we’ll get the values after we decrypt the document; the Tag being used only for the search purpose.
Being a deterministic scheme, an adversary might perform a statistical analysis. To avoid that, we recommend to use this scheme when you want to search by multiple fields, example:  FirstName, LastName and ZipCode and when those fields/tags combined has very high cardinality, ideally unique per collection. In this way it will provide the same security level as RND.


## Deterministic encryption scheme (DET)

Deterministic encryption scheme is used to encrypt Tag values and letting the cloud server to run equality or non-equality queries. It is called deterministic because for the same encryption key and same plain text, it will always have the same cipher text as the output. For this scheme we use AES and/or Camellia block ciphers in CBC mode with synthetic IV (initial vector).
Being a deterministic scheme, an adversary might perform a statistical analysis. To avoid that, we recommend to use this scheme when Tags values have high cardinality, ideally unique per collection. Example of such values: SSN(Social Security Number), Email, etc; such values are usually unique per collection. In this way it will provide the same security level as RND.

## Order Preserving Encryption (OPE)

This is a deterministic encryption scheme and it is used to encrypt Tag values and letting the cloud server to run equality or non-equality queries but also range queries. This scheme is very efficient because it let the server create indexes and make queries as efficiently as for unencrypted data. Besides the equality, the encrypted values leaks also the order which can be insecure if it is encrypted values with low cardinality. Therefor, we recommend to use this scheme to Tags values with high cardinality, example: DateTime. In this way it will provide a strong security level.

(The algorithm itself is owned and created by Dotissi Development SRL and we'll release in near future a paper which will describe how it internally works.)

## Randomized Order Preserving encryption scheme (ROPE)

This is a probabilistic encryption scheme and is used to encrypt Tag values and letting the cloud server to run equality or non-equality queries but also range queries. This scheme is very efficient because it let the server create indexes and make queries as efficiently as for unencrypted data.
It is probabilistic because the cipher text is randomized, so for the same plain text and same encryption key, the cipher text will be different (with a certain probability) for consecutive calls of the algorithm. In this way there will be no frequency information leaked, however the order is still preserved, so if we have 2 messages a and b if a<b then Enc(a)<Enc(b). As noticed, the server cannot execute directly equality queries since Enc(a) most likely is different on each run however the equality queries are still possible by transforming the client equality query in a range query. (See the implementation of IQryptParse and IQryptSiaqodb). From security perspective, this scheme is recommended to encrypt Tags with medium to high cardinality.

(The algorithm itself is owned and created by Dotissi Development SRL and we'll release in near future a paper which will describe how it internally works.)

## Client side bitmap indexes (BITMAP)

Bitmap indexes work well for low-cardinality tags, which have a modest number of distinct values, either absolutely, or relative to the number of records/documents that contain the data. The extreme case of low cardinality is Boolean data (True, False). Bitmap indexes(compared with B-Tree structures) are very efficient about storage; for example if we have 1 million JSON documents, the total index size for holding a tag value is about 100KB. If we have a Boolean tag it will be needed to store 2 indexes, one for True values and one for False values so in total we will have 200KB. Usually bitmap indexes are kept in RAM; for a server, nowadays, this amount of data means nothing, but if we keep it on the client and if cardinality is higher, then storage may be problematic. Fortunately the bitmap indexes can be compressed and reduce drastically its size. IQrypt uses EWAH described [here](http://arxiv.org/pdf/0901.3751v6.pdf)  and thanks to it, we can use bitmap indexes only on the client side. So basically this type of index is fully encrypted on the client side with RND encryption scheme and stored encrypted on the server. When we run a query for a Tag value encrypted with this scheme,  we will execute in fact 2 queries:one that download the entire (compressed and encrypted) bitmap index and one to get the documents that matches the query by providing to the server the identifiers got from bitmap indexes.( As an idea, we tested on many real world scenarios and the entire index compressed with EWAH for low cardinality tag values usually remain under 10KB).
So we recommend to use this scheme if you have low cardinality tag values like Boolean, Sex, Statuses or even medium cardinality like for example Salary or Country if there is a decent total amount of documents stored in a collection. Since it is encrypted with RND it offers same degree of security as RND scheme.

## Practical example

Let's suppose you store a Patient record in a JSON document like this:

```JSON

{
  "Key": "194f0fb0-7d1e-45c5-a71c-400342ccf422",
  "SSN": "003-62-5913",
  "FirstName": "John",
  "LastName": "Doe",
  "Gender": "Male",
  "Location": "2nd Floor, Building A",
  "Building": "A",
  "Floor": 2,
  "LastVisitDate": "2015-11-18T08:10:00",
  "AgendaHourMinute": "08:10",
  "Treatment": "Treatment: Lorem ipsum",
  "Status": "Stable",
  "Doctor": "Josh Pollock",
  "PatientDetailsData": {
    "Blood_Gas": {
      "pH": "7.32",
      "pCO2": "34",
      "HCO3": "26",
      "AG": "13",
      "p02": "78"
    },
    "BloodData": {
      "Hemoglobin": "45 gb/dll",
      "Hematocrit": "23%",
      "WBC": "12 ul",
      "Platelets": "34/mcl"
    },
    "General_Chemistry": {
      "Na": "56 mEq/L",
      "K": "23 mEq/L",
      "Glucose": "26 mg/dl",
      "Creatine": "13 mg/dl",
      "BUN": "78 mg/dl"
    },
    "UrinalysisData": {
      "Protien": "(-)",
      "Sugar": "(+)",
      "OccultBlood": "(-)"
    },
    "Progress_Notes": [
      {
        "Month": "Feb",
        "Date": "14",
        "Note": "Patient admitted complaining of abdominal pain.   Administered 20 ml of Trx.  Will plan to review condition in 6 hours.   Submitted request for blood work 2/15.  Dr Pollock will be..."
      },
      {
        "Month": "Feb",
        "Date": "15",
        "Note": "Current treatment plan appears to be progressing well.  Submitted dietary change request.   Have requested that patient remain on modified diet for 4 days.  Patient family..."
      },
    ],
    "Intake_Output": [
      {
        "Date": "2/11",
        "Intake": "750ml",
        "Output": "680ml"
      },
      {
        "Date": "2/12",
        "Intake": "520ml",
        "Output": "540ml"
      },
      {
        "Date": "2/13",
        "Intake": "820ml",
        "Output": "760ml"
      },
      {
        "Date": "2/14",
        "Intake": "500ml",
        "Output": "520ml"
      }
    ],
    "Admission_Records": {
      "Doctor": "Dr. John ollock",
      "HealthcareProvider": "AETNA",
      "Address": "4122 SE 1st PL    Newcastle, WA 98056",
      "Phone": "123.456.7890",
      "DateAdmitted": "Feb. 10, 2015",
      "DateDischarged": "N/A"
    }
  },
  "Version": null,
  "IsVisited": false
}
```

The goal is that this entire JSON document will be encrypted with RND encryption scheme, but RND is not a searchable encryption scheme so we'll need to adjust the data model.

At the client side, before the JSON document is sent to the cloud database and before being encrypted, IQrypt extracts the keywords as {key,value} pairs that you will need to search by and those keywords( we will call further Tags) will be encrypted with different encryption schemes or even let them plain text if does not reveal sensitive information. 

Supposing you want to search by Social Security Number (SSN), VisitDate, Building and Status, so it means we should extract that info as Tags, so our data model will look like this:

```JSON
{
 "Key": "194f0fb0-7d1e-45c5-a71c-400342ccf422", 

 "DocContent":{
      "Key": "194f0fb0-7d1e-45c5-a71c-400342ccf422",
      "SSN": "003-62-5913",
      "FirstName": "John",
      "LastName": "Doe",
      "Gender": "Male",
      "Location": "2nd Floor, Building A",
      "Building": "A",
      "Floor": 2,
      "LastVisitDate": "2015-11-18T08:10:00",
      "AgendaHourMinute": "08:10",
      "Treatment": "Treatment: Lorem ipsum",
      "Status": "Stable",
      "Doctor": "Josh Pollock",
      "PatientDetailsData": {
        "Blood_Gas": {
          "pH": "7.32",
          "pCO2": "34",
          "HCO3": "26",
          "AG": "13",
          "p02": "78"
        },
        "BloodData": {
          "Hemoglobin": "45 gb/dll",
          "Hematocrit": "23%",
          "WBC": "12 ul",
          "Platelets": "34/mcl"
        },
        "General_Chemistry": {
          "Na": "56 mEq/L",
          "K": "23 mEq/L",
          "Glucose": "26 mg/dl",
          "Creatine": "13 mg/dl",
          "BUN": "78 mg/dl"
        },
        "UrinalysisData": {
          "Protien": "(-)",
          "Sugar": "(+)",
          "OccultBlood": "(-)"
        },
        "Progress_Notes": [
          {
            "Month": "Feb",
            "Date": "14",
            "Note": "Patient admitted complaining of abdominal pain.   Administered 20 ml of Trx.  Will plan to review condition in 6 hours.   Submitted request for blood work 2/15.  Dr Pollock will be..."
          },
          {
            "Month": "Feb",
            "Date": "15",
            "Note": "Current treatment plan appears to be progressing well.  Submitted dietary change request.   Have requested that patient remain on modified diet for 4 days.  Patient family..."
          },
        ],
        "Intake_Output": [
          {
            "Date": "2/11",
            "Intake": "750ml",
            "Output": "680ml"
          },
          {
            "Date": "2/12",
            "Intake": "520ml",
            "Output": "540ml"
          },
          {
            "Date": "2/13",
            "Intake": "820ml",
            "Output": "760ml"
          },
          {
            "Date": "2/14",
            "Intake": "500ml",
            "Output": "520ml"
          }
        ],
        "Admission_Records": {
          "Doctor": "Dr. John ollock",
          "HealthcareProvider": "AETNA",
          "Address": "4122 SE 1st PL    Newcastle, WA 98056",
          "Phone": "123.456.7890",
          "DateAdmitted": "Feb. 10, 2015",
          "DateDischarged": "N/A"
        }
      },
      "Version": null,
      "IsVisited": false
    },

   "SSN":"003-62-5913",
   "LastVisitDate":"2015-11-18T08:10:00", 
   "Building": "A" 
   "Status": "Stable"
}
```
The JSON doc now has 3 main properties:

* Key which is unique identifier within document Collection/Bucket
* DocContent which is the initial JSON document
* Tags: ('SSN', 'LastVisitDate', 'Building' and 'Status')

Now, in fact, the data within Tags is stored redundantly (but this should not be a big overhead).  Usually by SSN we will need to make Equality queries, so we will encrypt it with DET encryption scheme. For VisitDate, we would need to make range queries so we'll encrypt with ROPE scheme, for Status, since it has low cardinality, we will encrypt with BITMAP and for Building, we'll let it plain text because it might not be considered sensitive information.

So to summarize, this is how IQrypt encrypts the record:

* JSON Document: RND scheme
* 'SSN' tag: DET scheme
* 'VisitDate' tag: ROPE scheme
* 'Status' tag: BITMAP scheme
* 'Building' tag: Plain text

To run queries, IQrypt will encrypt the query field value , at the client side, and then sent the encrypted value to server to compare against its records. 

Example of MongoDB query without IQrypt:
```Java
db.patients.find( { SSN: "003-62-5913"} )

```

And now using IQrypt:
```Java
db.patients.find( { SSN: "ljqEksrWV93kbQWedSNDlw9DEMFUIvZzz5uNRNwZONQ="} )

```
Now, MongoDB will return the record for SSN="003-62-5913" without knowing its value. 

IQrypt also allows you to encrypt Tag names, so the server has no information of what might store that Tag, example of such a query would be:

```Java
db.patients.find( { "A234BCF434DDAD67": "ljqEksrWV93kbQWedSNDlw9DEMFUIvZzz5uNRNwZONQ="} )

```
where "A234BCF434DDAD67" is the encrypted value for "SSN".

Now let's make a range query for LastVisitDate(without IQrypt):
```Java
db.patients.find( { $and: [ { LastVisitDate: { $gt: "2015-11-15T08:10:00" } }, { LastVisitDate: { $lt: "2015-12-31T08:10:00"  } } ] } )
```
And now using IQrypt:

```Java
db.patients.find( { $and: [ { LastVisitDate: { $gt: "@@@2102291610482 @@@2101986497510" } }, { LastVisitDate: { $lt: "@@@2102291611547 @@@2101994746036"  } } ] } )

```

Now, MongoDB will return all records/documents that has LastVisitDate between 2015-11-15 and 2015-12-31 without knowing its values.

## License

* Open Source License

This version of IQrypt is licensed under the terms of the Open Source GPL v3.0 license. 

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.If not, see http://www.gnu.org/licenses/gpl.txt

* Alternate Licensing

Commercial and OEM Licenses are available for an alternate download of IQrypt.
This is the appropriate option if you are creating proprietary applications and you are 
not prepared to distribute and share the source code of your application under the 
GPL v3 license. Please visit http://iqrypt.com/#pricing for more details.




