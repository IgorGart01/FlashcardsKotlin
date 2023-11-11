package flashcards
import java.io.File
var log = ""
fun printSaveLog(string: String){
    log += string + "\n"
    println(string)
}
class Card(){
    var card: String = ""
    var deff: String = ""
    var errOO: Int = 0
}
fun adding (cardBox: List<Card>): Card{
    var cardOP = Card()
    var termin = ""
    var def = ""
    do {
        printSaveLog("The card:")
        val _termin = readln().apply { log += this + "\n" }
        if (cardBox.any { it.card == _termin }) {
            printSaveLog("The card \"$_termin\" already exists.")
            return cardOP
        } else termin = _termin
    } while (cardBox.any { it.card == _termin })
    do {
        printSaveLog("The definition of the card:")
        val _def = readln().apply { log += this + "\n" }
        if (cardBox.any { it.deff == _def }) {
            printSaveLog("The definition \"$_def\" already exists.")
            return cardOP
        } else def = _def
    }while (cardBox.any { it.deff == _def })
    cardOP.card = termin
    cardOP.deff = def
    return cardOP
}
fun removing (cardBox: List<Card>):String{
    printSaveLog("Which card?")
    val card = readln().apply { log += this + "\n" }
    if (cardBox.any{ it.card == card}) return card
    else { printSaveLog("Can't remove \"$card\": there is no such card."); return "" }
}
fun exPort (cardBox: List<Card>, string: String){
    val fileName = File(string)
    fileName.writeText("")
    for (i in cardBox) fileName.appendText("${i.card}:${i.deff}=${i.errOO}\n")
    printSaveLog("${cardBox.size} cards have been saved.")
}
fun hardest (cardBox: List<Card>) {
    var countErrCard = 0
    var cardOne = ""
    if (cardBox.size == 0) { printSaveLog("There are no cards with errors."); return }
    val maxError = cardBox.maxOf { it.errOO }
    if (maxError == 0) { printSaveLog("There are no cards with errors."); return }
    cardBox.forEach { if (it.errOO == maxError) countErrCard++; cardOne = it.card }
    if (countErrCard == 1) printSaveLog("The hardest card is \"$cardOne\". You have $maxError errors answering it.")
    if (countErrCard > 1) {
        var errorText = "The hardest cards are "
        cardBox.forEach { if (it.errOO == maxError) errorText += "\"${ it.card }\", " }
        printSaveLog(errorText.dropLast(2) + ". You have $maxError errors answering them.")
    }
}
fun saveLog (){
    printSaveLog("File name:")
    val fileSaveLogName = File(readln().apply { log += this + "\n" })
    fileSaveLogName.writeText("")
    printSaveLog("The log has been saved.")
    fileSaveLogName.appendText(log)
}
fun imPort (cardBoxBase: List<Card>, string: String): MutableList<Card>{
    var cardBox = mutableListOf<Card>()
    cardBox.addAll(cardBoxBase)
    var ju = 0
    try {
        val fileName = File(string).readLines()
        for (i in fileName){
            if (!cardBox.any { it.card == (i.split(":").first()) }  ){
                cardBox.add(Card().apply { this.card = i.split(":").first()
                    this.deff = i.split(":").last().split("=").first()
                    this.errOO = i.split("=").last().toInt()})
                ju++
            } else {
                cardBox.find { it.card == i.split(":").first() }?.deff = i.split(":").last().split("=").first()
                cardBox.find { it.card == i.split(":").first() }?.errOO = i.split("=").last().toInt()
                ju++
            }
        }
    } catch (e: Exception) { printSaveLog("File not found."); return cardBox }
    if (ju == 0) return cardBox
    else {
        printSaveLog("${ju} cards have been loaded.")
        return cardBox
    }
}
fun main(array: Array<String>){
    var cardBox = mutableListOf<Card>()
    if (array.contains("-import")) cardBox = imPort(cardBox, array[array.indexOf("-import") + 1])
    do {
        printSaveLog("Input the action (add, remove, import, export, ask, exit, log, hardest card, reset stats):")
        val iNPUT = readln().apply { log += this + "\n" }
        when (iNPUT){
            "log" -> saveLog()
            "reset stats" -> { cardBox.forEach { it.errOO = 0 }; printSaveLog("Card statistics have been reset.") }
            "hardest card" -> hardest(cardBox)
            "add" -> {
                val k = adding(cardBox)
                if (k.card == "")  continue
                else { cardBox.add(k); printSaveLog("The pair (\"${k.card}\":\"${k.deff}\") has been added.")}
            }
            "remove" -> {
                val remCard = removing(cardBox)
                if (remCard != "") {
                    if (cardBox.any { it.card == remCard }) cardBox.remove(cardBox.find { it.card == remCard })
                    printSaveLog("The card has been removed.")
                } else continue
            }
            "exit" -> {
                if (array.contains("-export")) exPort(cardBox, array[array.indexOf("-export") + 1])
                printSaveLog("Bye bye!");break }
            "import" -> {
                printSaveLog("File name:")
                cardBox = imPort(cardBox, readln().apply { log += this + "\n" })
            }
            "export" -> {
                printSaveLog("File name:")
                exPort(cardBox, readln().apply { log += this + "\n" })
            }
            "ask" -> {
                printSaveLog("How many times to ask?").run { log += this.toString() }
                val quantityCard = readln().apply { log += this + "\n" }.toInt()
                repeat(quantityCard) {
                    val shuf = cardBox.shuffled().first()
                    printSaveLog("Print the definition of \"${shuf.card}\":")
                    val hold = readln().apply { log += this + "\n" }
                    if (shuf.deff == hold) printSaveLog("Correct!")
                    else {
                        shuf.errOO++
                        if (cardBox.any { it.deff == hold }) printSaveLog("Wrong. The right answer is \"${shuf.deff}\", " +
                                "but your definition is correct for \"${cardBox.filter { hold == it.deff }.first().card}\".")
                        else printSaveLog("Wrong. The right answer is \"${shuf.deff}\"")
                    }
                }
            }
        }
    } while (true)
}