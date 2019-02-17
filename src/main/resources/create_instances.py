import json
import requests
from dateutil.parser import parse

baseUrl = "https://query.wikidata.org/sparql?format=json&query="


queryPublicationDate = "select ?game (min(?date) as ?pubDate) where {?game wdt:P31 wd:Q7889 . ?game wdt:P577 ?date . } group by ?game"
req = requests.get(baseUrl + queryPublicationDate).text
jsonReq = json.loads(req)
publicationDateDict = {}
for result in jsonReq["results"]["bindings"]:
	publicationDateDict[result["game"]["value"]] = result["pubDate"]["value"]
#print(publicationDateDict)


queryHasPrevious = "construct { ?gameRecent wdt:hasPreviousGame ?gameOld} where {?gameRecent wdt:P31 wd:Q7889 . ?gameOld wdt:P31 wd:Q7889 . ?gameRecent wdt:P179 ?serie . ?gameOld wdt:P179 ?serie . ?gameRecent wdt:P577 ?recentDate . ?gameOld wdt:P577 ?oldDate . FILTER ( ?recentDate > ?oldDate)}"
req = requests.get(baseUrl + queryHasPrevious).text
jsonReq = json.loads(req)
#print(jsonReq["results"]["bindings"][0]["subject"]["value"])
#print(jsonReq["results"]["bindings"][0]["predicate"]["value"])
previousGameDict = {}
for result in jsonReq["results"]["bindings"]:
	try:
		if(parse(publicationDateDict[result["subject"]["value"]]) > parse(publicationDateDict[result["object"]["value"]])):
			if(not result["subject"]["value"] in previousGameDict):
				previousGameDict[result["subject"]["value"]] = []
			l = previousGameDict[result["subject"]["value"]]
			l.append(result["object"]["value"])
			previousGameDict[result["subject"]["value"]] = l
	except ValueError :
		continue

#print(len(previousGameDict))


with open('uriGame.json') as f:
    data = json.load(f)

f = open("categories.rdf", "r")
print(f.read())

gamePerYearList = []
topOfYear = []
for i in range(1950, 2020):
	gamePerYearList.append([])
	#print('<Year rdf:ID="' + str(i) + '">')
	#print('<value>' + str(i) + '</value>')
	#print('</Year>')
for game in data:
	if("uri" in game and game["uri"] in publicationDateDict):
		if(len(publicationDateDict[game["uri"]].split('-')) > 1):
			year = publicationDateDict[game["uri"]].split('-')[0]
			gamePerYearList[int(year) - 1950].append([game["uri"], int(game["owners"].split(' .. ')[1].replace(',',''))])
for i in range(len(gamePerYearList)):
	gamePerYearList[i].sort(key=lambda tup: tup[1])
	for j in range(0, min(10, len(gamePerYearList[i]))):
		topOfYear.append(gamePerYearList[i][-j-1][0])

for game in data:
	if("uri" in game):
		print('<VideoGame rdf:ID="' + game["uri"].split("http://www.wikidata.org/entity/")[1] + '">')
		print('<owl:sameAs>' + game["uri"] + '</owl:sameAs>')
		print('<wdt:hasName>' + game["name"] + '</wdt:hasName>')
		print('<wdt:numberOfsells>'+ game["owners"].split(' .. ')[1].replace(',','') +'</wdt:numberOfsells>')
		print('<wdt:hasUserScore>'+ str(game["userscore"]) +'</wdt:hasUserScore>')
		for genre in game["genre"]:
			print('<wdt:hasGenre rdf:resource="#'+ genre.replace('&', "And") +'"/>')

		if(game["uri"] in publicationDateDict):
			print("<wdt:P577>" + publicationDateDict[game["uri"]] + "</wdt:P577>")
		if(game["uri"] in previousGameDict):
			for previousGame in previousGameDict[game["uri"]]:
				print('<wdt:hasPreviousGame rdf:resource="#' + previousGame.split("http://www.wikidata.org/entity/")[1] + '"/>')
		if(game["uri"] in topOfYear):
			print('<wdt:topOfYear>'+ publicationDateDict[game["uri"]].split('-')[0] +'</wdt:topOfYear>')

		print('</VideoGame>')

print("</rdf:RDF>")