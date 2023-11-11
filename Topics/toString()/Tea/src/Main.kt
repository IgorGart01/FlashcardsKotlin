open class Tea(val cost: Int, val volume: Int) {
    override fun toString(): String {
        return "cost=$cost, volume=$volume"
    }
}

class BlackTea(val cost: Int, val volume: Int){
    override  fun  toString(): String = "BlackTea{cost=$cost, volume=$volume}"

}