package com.example.flow

import co.paralleluniverse.fibers.Suspendable
import com.example.contract.IOUContract
import com.example.state.IOUState
import net.corda.core.contracts.StateAndContract
import net.corda.core.flows.FinalityFlow
import net.corda.core.flows.FlowLogic
import net.corda.core.flows.InitiatingFlow
import net.corda.core.flows.StartableByRPC
import net.corda.core.transactions.SignedTransaction
import net.corda.core.transactions.TransactionBuilder

/**
 * Created by sangalli on 5/10/17.
 */

//TODO make own contract for owning of tickets or alter IOUContract to fit
object CreateSignedTransactionFlow
{
    @InitiatingFlow
    @StartableByRPC
    class createSignedTx(val iouState: IOUState) : FlowLogic<SignedTransaction>()
    {
        //No need to send to another party, just needs to be issued into existence
        @Suspendable
        override fun call(): SignedTransaction
        {
            val notary = serviceHub.networkMapCache.notaryIdentities.single()
            val create = IOUContract.Commands.Create()

            val txBuilder = TransactionBuilder(notary).withItems(
                    StateAndContract(iouState, IOUContract.IOU_CONTRACT_ID), create)
            txBuilder.verify(serviceHub)
            val ptx = serviceHub.signInitialTransaction(txBuilder)

            return subFlow(FinalityFlow(ptx))
        }

    }
}
