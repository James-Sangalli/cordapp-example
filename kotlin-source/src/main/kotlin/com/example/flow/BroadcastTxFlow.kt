package com.example.flow

import co.paralleluniverse.fibers.Suspendable
import net.corda.core.flows.FlowLogic
import net.corda.core.flows.InitiatingFlow
import net.corda.core.flows.StartableByRPC
import net.corda.core.transactions.SignedTransaction

//This flow is used to broadcast the users signed transaction which can represent an asset
//for example a ticket, you only need to prove ownership of the ticket unless you are trading it
//in which case you would need to broadcast it

object BroadcastTxFlow
{
    @InitiatingFlow
    @StartableByRPC
    class BroadcastTx(val SignedTx : SignedTransaction) : FlowLogic<SignedTransaction>()
    {
        //No need to send to another party, just needs to be issued into existence
        @Suspendable
        override fun call() : SignedTransaction
        {
            serviceHub.recordTransactions(SignedTx)
            return SignedTx
        }

    }
}
