data class Client(val name: String = readln(), val age: Int = readln().toInt()){
    val balance: Int = readln().toInt()


}

fun main() {

    println (Client() == Client())
}